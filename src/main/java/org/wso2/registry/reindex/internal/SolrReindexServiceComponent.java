/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.registry.reindex.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.registry.reindex.SolrReindexMXBeanImpl;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.wso2.carbon.utils.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @scr.component name="org.wso2.registry.reindex.internal.SolrReindexServiceComponent" immediate="true"
 * @scr.reference name="configuration.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="user.realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */

public class SolrReindexServiceComponent {
    private static Log log = LogFactory.getLog(SolrReindexServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        log.info("Starting SolrReindexServiceComponent ");
        try {
            MBeanServer mbs = ManagementFactory.getMBeanServer();
            ObjectName name = new ObjectName("org.wso2.registry.reindex:type=SolrReindexMXBean");
            SolrReindexMXBeanImpl reindexMXBean = new SolrReindexMXBeanImpl();
            mbs.registerMBean(reindexMXBean, name);
        } catch (Exception e) {
            String msg = "Failed to initialize the SolrReindexServiceComponent.";
            log.error(msg, e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        log.info("Stopping SolrReindexMXBean");
    }

    protected void setConfigurationContextService(ConfigurationContextService contextService) {
        SolrReindexDataHolder.setConfigContext(contextService.getServerConfigContext());
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
        SolrReindexDataHolder.setConfigContext(null);
    }

    protected void setRegistryService(RegistryService registryService) {
        if (registryService != null && log.isDebugEnabled()) {
            log.debug("Registry service initialized");
        }
        SolrReindexDataHolder.setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        SolrReindexDataHolder.setRegistryService(null);
    }

    protected void setRealmService(RealmService realmService) {
        if (realmService != null && log.isDebugEnabled()) {
            log.debug("Realm service initialized");
        }
        SolrReindexDataHolder.setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        SolrReindexDataHolder.setRealmService(null);

    }
}
