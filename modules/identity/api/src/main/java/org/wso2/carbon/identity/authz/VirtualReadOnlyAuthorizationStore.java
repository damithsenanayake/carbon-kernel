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
package org.wso2.carbon.identity.authz;

import org.wso2.carbon.identity.authn.PrivilegedGroup;
import org.wso2.carbon.identity.authn.PrivilegedUser;
import org.wso2.carbon.identity.authz.spi.PermissionSearchCriteria;
import org.wso2.carbon.identity.authz.spi.RoleSearchCriteria;

import java.util.List;

public interface VirtualReadOnlyAuthorizationStore<U extends PrivilegedUser<G, R>,
        G extends PrivilegedGroup<U, R>,
        R extends PrivilegedRole<U, G>> {

    /**
     * @param permissionIdentifier
     * @return
     * @throws AuthorizationStoreException
     */
    public Permission getPermission(PermissionIdentifier permissionIdentifier)
            throws AuthorizationStoreException;

    /**
     * @param searchCriteria
     * @return
     * @throws AuthorizationStoreException
     */
    public List<Permission> getPermissions(
            PermissionSearchCriteria searchCriteria)
            throws AuthorizationStoreException;

    /**
     * @param roleIdentifier
     * @return
     * @throws AuthorizationStoreException
     */
    public R getRole(RoleIdentifier roleIdentifier)
            throws AuthorizationStoreException;

    /**
     * @param searchCriteria
     * @return
     * @throws AuthorizationStoreException
     */
    public List<R> getRoles(RoleSearchCriteria searchCriteria)
            throws AuthorizationStoreException;
}
