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
package org.wso2.carbon.kernel.deployment.service;

import org.wso2.carbon.kernel.deployment.Artifact;
import org.wso2.carbon.kernel.deployment.ArtifactType;
import org.wso2.carbon.kernel.deployment.Deployer;
import org.wso2.carbon.kernel.deployment.exception.CarbonDeploymentException;
import org.wso2.carbon.kernel.internal.deployment.CarbonDeploymentService;
import org.wso2.carbon.kernel.internal.deployment.DeploymentEngine;

import java.io.File;
import java.util.ArrayList;

/**
 * Custom Deployment Service class.
 *
 * @since 5.0.0
 */
public class CustomDeploymentService extends CarbonDeploymentService {

    private DeploymentEngine deploymentEngine;

    public CustomDeploymentService(DeploymentEngine deploymentEngine) {
        super(deploymentEngine);
        this.deploymentEngine = deploymentEngine;
    }

    @Override
    public void deploy(String artifactPath, ArtifactType artifactType)
            throws CarbonDeploymentException {
        try {
            super.deploy("fake/path", artifactType);
        } catch (CarbonDeploymentException e) {
            //ignore
        }
        Artifact artifact = new Artifact(new File(artifactPath));
        artifact.setType(artifactType);
        ArrayList<Artifact> artifactList = new ArrayList<>();
        artifactList.add(artifact);
        Deployer deployer = deploymentEngine.getDeployer(artifactType);

        if (deployer == null) {
            throw new CarbonDeploymentException("Unknown artifactType : " + artifactType);
        }
        deploymentEngine.deployArtifacts(artifactList);
    }

    @Override
    public void undeploy(Object key, ArtifactType artifactType) throws CarbonDeploymentException {
        try {
            super.undeploy("fake.key", artifactType);
        } catch (CarbonDeploymentException e) {
            // ignore
        }
        Deployer deployer = deploymentEngine.getDeployer(artifactType);

        if (deployer == null) {
            throw new CarbonDeploymentException("Unknown artifactType : " + artifactType);
        }
        deployer.undeploy(key);
    }

    @Override
    public void redeploy(Object key, ArtifactType artifactType) throws CarbonDeploymentException {

        try {
            super.redeploy("fake.key", artifactType);
        } catch (CarbonDeploymentException e) {
            // ignore
        }
        Deployer deployer = deploymentEngine.getDeployer(artifactType);
        if (deployer == null) {
            throw new CarbonDeploymentException("Unknown artifactType : " + artifactType);
        }

        Artifact deployedArtifact = deploymentEngine.getDeployedArtifact(artifactType, key);

        if (deployedArtifact == null) {
            throw new CarbonDeploymentException("Cannot find artifact with the key : " + key);
        }

        deployer.update(deployedArtifact);
    }
}
