package org.wso2.registry.reindex.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.common.services.RegistryAbstractAdmin;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.indexing.IndexingConstants;
import org.wso2.carbon.registry.indexing.IndexingManager;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.Calendar;
import java.util.Date;

public class ReindexAdminService extends RegistryAbstractAdmin implements IReindexService {

    private static Log log = LogFactory.getLog(ReindexAdminService.class);

    private Date getOldDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 01);
        calendar.set(Calendar.YEAR, 2000);
        return calendar.getTime();
    }

    public void reindexSolrData() {
        log.info("Reindexing process is started by : " + getUsername());
        Registry registry = getRootRegistry();
        try {
            if (registry.resourceExists(IndexingConstants.LAST_ACCESS_TIME_LOCATION)) {
                registry.delete(IndexingConstants.LAST_ACCESS_TIME_LOCATION);
            }
            UserRegistry userRegistry = (UserRegistry) getGovernanceUserRegistry();
            IndexingManager.getInstance().setLastAccessTime(userRegistry.getTenantId(), getOldDate());
            log.info("Reindexing process completed for Tenant : " + getTenantDomain());
        } catch (RegistryException e) {
            log.error("Error while reindexing resource");
        }

    }
}
