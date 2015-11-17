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
package org.wso2.carbon.context.login.identifiers;

import org.wso2.carbon.context.login.commons.Visibility;

public class GroupIdentifier extends EntityIdentifier {

    private Visibility visibility;
    private StoreIdentifier storeIdentifier;

    public GroupIdentifier(StoreIdentifier storeIdentifier, String value, Visibility visibility) {
        super(value);
        this.storeIdentifier = storeIdentifier;
        this.visibility = visibility;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public StoreIdentifier getStoreIdentifier() {
        return storeIdentifier;
    }

}
