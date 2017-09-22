/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.registry.reindex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.utils.RegistryUtils;
import org.wso2.registry.reindex.internal.SolrReindexDataHolder;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.indexing.IndexingConstants;
import org.wso2.carbon.registry.indexing.IndexingManager;
import org.wso2.carbon.user.api.Tenant;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.Calendar;
import java.util.Date;

public class SolrReindexMXBeanImpl implements SolrReindexMXBean {

    private static Log log = LogFactory.getLog(SolrReindexMXBeanImpl.class);

    public void reindexTenantSolrData(String tenantDomain) {
        try {
            PrivilegedCarbonContext.startTenantFlow();
            int tenantId = SolrReindexDataHolder.getRealmService().getTenantManager().getTenantId(tenantDomain);
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(tenantDomain, true);
            reindex(tenantId);
            log.info("Reindexed Solr data for Tenant : " + tenantDomain);
        } catch (UserStoreException e) {
            log.error("Error while reindexing resource");
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }


    public void reindexAllTenantSolrData() {
        reindexSuperTenantSolrData();
        Tenant[] allTenants = new Tenant[0];
        try {
            allTenants = SolrReindexDataHolder.getRealmService().getTenantManager().getAllTenants();
        } catch (UserStoreException e) {
            log.info("Error while loading tenants");
        }
        for (Tenant tenant : allTenants) {
            PrivilegedCarbonContext.startTenantFlow();
            try {
                int tenantId = tenant.getId();
                PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantId, true);
                RegistryUtils.initializeTenant(SolrReindexDataHolder.getRegistryService(), tenantId);
                reindex(tenantId);
                log.info("Reindexed Solr data for Tenant : " + tenant.getDomain());
            } catch (RegistryException e) {
                log.error("Error while initializing Tenant ");
            } finally {
                PrivilegedCarbonContext.endTenantFlow();
            }
        }

    }

    public void reindexSuperTenantSolrData() {
        try {
            PrivilegedCarbonContext.startTenantFlow();
            String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(tenantDomain, true);
            reindex(MultitenantConstants.SUPER_TENANT_ID);
            log.info("Reindexed Solr data for supper tenant");
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    private void reindex(int tenantId) {
        try {
            Registry registry = SolrReindexDataHolder.getRegistryService().getRegistry(CarbonConstants.REGISTRY_SYSTEM_USERNAME);
            if (registry.resourceExists(IndexingConstants.LAST_ACCESS_TIME_LOCATION)){
                registry.delete(IndexingConstants.LAST_ACCESS_TIME_LOCATION);
            }
            IndexingManager.getInstance().setLastAccessTime(tenantId, getOldDate());
        } catch (RegistryException e) {
            log.error("Error while reindexing resource");
        }
    }

    private Date getOldDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 01);
        calendar.set(Calendar.YEAR, 2000);
        return calendar.getTime();
    }

}
