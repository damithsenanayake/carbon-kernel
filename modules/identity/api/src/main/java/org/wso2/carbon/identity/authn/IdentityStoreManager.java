/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.identity.authn;

import org.wso2.carbon.identity.account.spi.ReadWriteLinkedAccountStore;
import org.wso2.carbon.identity.claim.ClaimManager;
import org.wso2.carbon.identity.commons.IdentityException;
import org.wso2.carbon.identity.config.spi.IdentityStoreConfig;
import org.wso2.carbon.identity.credential.spi.CredentialStore;

public interface IdentityStoreManager extends ReadOnlyIdentityStoreManager {

    public void init(IdentityStoreConfig primaryStoreConfig, ClaimManager claimManager,
                     ReadWriteLinkedAccountStore linkedAccountStore, CredentialStore credentialStore);

    /**
     * @param storeConfig
     * @throws IdentityException
     */
    public void addIdentityStore(IdentityStoreConfig storeConfig) throws IdentityException;

    /**
     * @param storeIdentifier
     * @throws IdentityException
     */
    public void dropIdentityStore(StoreIdentifier storeIdentifier) throws IdentityException;

    /**
     * @param storeConfig
     * @throws IdentityException
     */
    public void updateIdentityStore(IdentityStoreConfig storeConfig) throws IdentityException;

}
