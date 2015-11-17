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
package org.wso2.carbon.kernel.deployment;

/**
 * This class represent the artifact type of an artifact.
 *
 * @since 5.0.0
 * @param <T> Artifact type
 */
public class ArtifactType<T> {
    private T type;

    public ArtifactType(T type) {
        this.type = type;
    }

    public T get() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other != null && this.getClass() == other.getClass() && this.type.equals(((ArtifactType) other).get());
    }
}
