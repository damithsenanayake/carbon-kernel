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

import java.util.Collections;
import java.util.List;

public class Role {

    private RoleIdentifier roleIdentifier;
    private List<Permission> permission;

    /**
     * @param roleIdentifier
     */
    public Role(RoleIdentifier roleIdentifier) {
        this.roleIdentifier = roleIdentifier;
    }

    /**
     * @param roleIdentifier
     * @param permission
     */
    public Role(RoleIdentifier roleIdentifier, List<Permission> permission) {
        this.roleIdentifier = roleIdentifier;
        this.permission = permission;
    }

    /**
     * @return
     */
    public RoleIdentifier getRoleIdentifier() {
        return roleIdentifier;
    }

    /**
     * @return
     */
    public List<Permission> getPermission() {
        return Collections.unmodifiableList(permission);
    }
}
