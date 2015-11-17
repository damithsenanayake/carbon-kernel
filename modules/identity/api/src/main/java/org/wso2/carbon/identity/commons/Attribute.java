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
package org.wso2.carbon.identity.commons;

import org.wso2.carbon.identity.authn.AttributeValue;

public class Attribute {

    private AttributeIdentifier attributeIdentifier;
    @SuppressWarnings("rawtypes")
    private AttributeValue attributeValue;

    /**
     * @param attributeIdentifier
     * @param attributeValue
     */
    @SuppressWarnings("rawtypes")
    public Attribute(AttributeIdentifier attributeIdentifier, AttributeValue attributeValue) {
        this.attributeValue = attributeValue;
        this.attributeIdentifier = attributeIdentifier;
    }

    /**
     * @return
     */
    public AttributeIdentifier getAttributeIdentifier() {
        return attributeIdentifier;
    }

    /**
     * @return
     */
    @SuppressWarnings("rawtypes")
    public AttributeValue getAttributeValue() {
        return attributeValue;
    }

}
