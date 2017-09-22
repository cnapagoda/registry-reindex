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

import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.CarbonUtils;

/**
 * Cache invalidating data holder
 */
public class SolrReindexDataHolder {

    private static ConfigurationContext configContext;
    private static RegistryService registryService;
    private static RealmService realmService;

    public static void setConfigContext(ConfigurationContext configContext) {
        SolrReindexDataHolder.configContext = configContext;
    }

    public static ConfigurationContext getConfigContext() {
        CarbonUtils.checkSecurity();
        return configContext;
    }

    public static RegistryService getRegistryService() {
        return registryService;
    }

    public static void setRegistryService(RegistryService registryService) {
        SolrReindexDataHolder.registryService = registryService;
    }

    public static RealmService getRealmService() {
        return realmService;
    }

    public static void setRealmService(RealmService realmService) {
        SolrReindexDataHolder.realmService = realmService;
    }
}