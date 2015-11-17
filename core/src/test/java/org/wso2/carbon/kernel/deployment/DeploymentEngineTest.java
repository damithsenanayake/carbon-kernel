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

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.wso2.carbon.kernel.deployment.deployers.CustomDeployer;
import org.wso2.carbon.kernel.deployment.exception.CarbonDeploymentException;
import org.wso2.carbon.kernel.deployment.exception.DeployerRegistrationException;
import org.wso2.carbon.kernel.deployment.exception.DeploymentEngineException;
import org.wso2.carbon.kernel.internal.deployment.DeploymentEngine;
import org.wso2.carbon.kernel.internal.deployment.RepositoryScanner;

import java.io.File;
import java.util.ArrayList;

/**
 * Deployment Engine Test class.
 *
 * @since 5.0.0
 */
public class DeploymentEngineTest extends BaseTest {
    private static final String CARBON_REPO = "carbon-repo";
    private static final String DEPLOYER_REPO = "carbon-repo" + File.separator + "text-files";
    private DeploymentEngine deploymentEngine;
    private CustomDeployer customDeployer;
    private ArrayList<Artifact> artifactsList;
    private RepositoryScanner repositoryScanner;

    /**
     * @param testName
     */
    public DeploymentEngineTest(String testName) {
        super(testName);

    }

    @BeforeTest
    public void setup() throws CarbonDeploymentException {
        customDeployer = new CustomDeployer();
        artifactsList = new ArrayList<>();
        Artifact artifact = new Artifact(new File(getTestResourceFile(DEPLOYER_REPO).getAbsolutePath()
                + File.separator + "sample1.txt"));
        artifact.setType(new ArtifactType<String>("txt"));
        artifactsList.add(artifact);
    }

    @Test(expectedExceptions = DeploymentEngineException.class,
            expectedExceptionsMessageRegExp = "Cannot find repository : .*")
    public void testUninitializedDeploymentEngine() throws DeploymentEngineException {
        DeploymentEngine engine = new DeploymentEngine("/fake/path");
        engine.start();
    }

    @Test
    public void testCarbonDeploymentEngine() throws DeploymentEngineException {
        deploymentEngine = new DeploymentEngine(getTestResourceFile(CARBON_REPO).getAbsolutePath());
        deploymentEngine.start();
        repositoryScanner = new RepositoryScanner(deploymentEngine);
    }

    @Test(expectedExceptions = DeployerRegistrationException.class,
            expectedExceptionsMessageRegExp = "Failed to add Deployer : Deployer Class Name is null")
    public void testDummyDeployer1() throws DeployerRegistrationException {
        deploymentEngine.registerDeployer(null);
    }

    @Test(expectedExceptions = DeployerRegistrationException.class,
            expectedExceptionsMessageRegExp = "Failed to add Deployer .*: missing 'directory' " +
                                              "attribute in deployer instance")
    public void testDummyDeployer2() throws DeployerRegistrationException {
        CustomDeployer dummy = new CustomDeployer();
        dummy.setLocation(null);
        deploymentEngine.registerDeployer(dummy);
    }

    @Test(expectedExceptions = DeployerRegistrationException.class,
            expectedExceptionsMessageRegExp = "Artifact Type for Deployer : .* is null")
    public void testDummyDeployer3() throws DeployerRegistrationException {
        CustomDeployer dummy = new CustomDeployer();
        dummy.setArtifactType(null);
        deploymentEngine.registerDeployer(dummy);
    }

    @Test(dependsOnMethods = {"testCarbonDeploymentEngine"})
    public void testAddDeployer() throws DeployerRegistrationException {
        deploymentEngine.registerDeployer(customDeployer);
        Assert.assertNotNull(deploymentEngine.getDeployer(customDeployer.getArtifactType()));
    }

    @Test(dependsOnMethods = {"testAddDeployer"})
    public void testRepositoryScanner() {
        repositoryScanner.scan();
        Assert.assertTrue(CustomDeployer.sample1Deployed);
    }

    @Test(dependsOnMethods = {"testAddDeployer"})
    public void testDeployArtifacts() {
        deploymentEngine.deployArtifacts(artifactsList);
        Assert.assertTrue(CustomDeployer.sample1Deployed);
    }

    @Test(dependsOnMethods = {"testDeployArtifacts"})
    public void testUpdateArtifacts() {
        deploymentEngine.updateArtifacts(artifactsList);
        Assert.assertTrue(CustomDeployer.sample1Updated);
    }

    @Test(dependsOnMethods = {"testUpdateArtifacts"})
    public void testUndeployArtifacts() {
        deploymentEngine.undeployArtifacts(artifactsList);
        Assert.assertFalse(CustomDeployer.sample1Deployed);
    }

    @Test
    public void testRemoveDummyDeployer() {
        try {
            CustomDeployer dummy = new CustomDeployer();
            dummy.setArtifactType(null);
            deploymentEngine.unregisterDeployer(dummy);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Artifact Type"));
        }
    }

    @Test(dependsOnMethods = {"testUndeployArtifacts"})
    public void testRemoveDeployer() throws DeploymentEngineException {
        deploymentEngine.unregisterDeployer(customDeployer);
        Assert.assertNull(deploymentEngine.getDeployer(customDeployer.getArtifactType()));
    }

}
