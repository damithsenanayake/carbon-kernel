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
package org.wso2.carbon.kernel.internal.deployment;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.kernel.CarbonRuntime;
import org.wso2.carbon.kernel.deployment.Deployer;
import org.wso2.carbon.kernel.deployment.DeploymentService;
import org.wso2.carbon.kernel.deployment.exception.DeployerRegistrationException;
import org.wso2.carbon.kernel.deployment.exception.DeploymentEngineException;

import java.util.ArrayList;
import java.util.List;

/**
 * This service component is responsible for initializing the DeploymentEngine and listening for deployer registrations.
 *
 * @since 5.0.0
 */
@Component(
        name = "org.wso2.carbon.kernel.internal.deployment.DeploymentEngineComponent",
        immediate = true
)

public class DeploymentEngineComponent {
    private static Logger logger = LoggerFactory.getLogger(DeploymentEngineComponent.class);

    private CarbonRuntime carbonRuntime;
    private DeploymentEngine deploymentEngine;
    private ServiceRegistration serviceRegistration;
    private List<Deployer> deployerList = new ArrayList<>();


    /**
     * This is the activation method of DeploymentEngineComponent. This will be called when its references are
     * satisfied.
     *
     * @param bundleContext the bundle context instance of the carbon core bundle used service registration, etc.
     * @throws Exception this will be thrown if an issue occurs while executing the activate method
     */

    @Activate
    public void start(BundleContext bundleContext) throws Exception {
        try {
            // Initialize deployment engine and scan it
            String carbonRepositoryLocation = carbonRuntime.getConfiguration().
                    getDeploymentConfig().getRepositoryLocation();
            deploymentEngine = new DeploymentEngine(carbonRepositoryLocation);

            logger.debug("Starting Carbon Deployment Engine {}", deploymentEngine);
            deploymentEngine.start();

            // Add deployment engine to the data holder for later usages/references of this object
            OSGiServiceHolder.getInstance().setCarbonDeploymentEngine(deploymentEngine);

            // Register DeploymentService
            DeploymentService deploymentService = new CarbonDeploymentService(deploymentEngine);
            serviceRegistration = bundleContext.registerService(DeploymentService.class.getName(),
                    deploymentService, null);

            logger.debug("Started Carbon Deployment Engine");

            // register pending deployers in the list
            for (Deployer deployer : deployerList) {
                try {
                    deploymentEngine.registerDeployer(deployer);
                } catch (Exception e) {
                    logger.error("Error while adding deployer to the deployment engine", e);
                }
            }

        } catch (DeploymentEngineException e) {
            String msg = "Could not initialize carbon deployment engine";
            logger.error(msg, e);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This is the deactivation method of DeploymentEngineComponent. This will be called when this component
     * is being stopped or references are satisfied during runtime.
     *
     * @throws Exception this will be thrown if an issue occurs while executing the de-activate method
     */

    @Deactivate
    public void stop() throws Exception {
        serviceRegistration.unregister();
    }

    /**
     * The is a dependency of DeploymentEngineComponent for deployer registrations from other bundles.
     * This is the bind method that gets called for deployer instance registrations that satisfy the policy.
     *
     * @param deployer the deployer instances that are registered as services.
     */
    @Reference(
            name = "carbon.deployer.service",
            service = Deployer.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unregisterDeployer"
    )
    protected void registerDeployer(Deployer deployer) {
        if (deploymentEngine != null) {
            try {
                deploymentEngine.registerDeployer(deployer);
            } catch (DeployerRegistrationException e) {
                logger.error("Error while adding deployer to the deployment engine", e);
            }
        } else { //carbon deployment engine is not initialized yet, so we keep them in a pending list
            deployerList.add(deployer);
        }
    }

    /**
     * This is the unbind method for the above reference that gets called for deployer instance un-registrations.
     *
     * @param deployer the deployer instances that are un-registered.
     */
    protected void unregisterDeployer(Deployer deployer) {
        try {
            deploymentEngine.unregisterDeployer(deployer);
        } catch (DeploymentEngineException e) {
            logger.error("Error while removing deployer from deployment engine", e);
        }
    }

    /**
     * The is another dependency of DeploymentEngineComponent for CarbonRuntime registrations from other bundles.
     * This is the bind method that gets called for CarbonRuntime instance registrations that satisfy the policy.
     *
     * @param carbonRuntime the carbonRuntime instances that are registered as services.
     */
    @Reference(
            name = "carbon.runtime.service",
            service = CarbonRuntime.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetCarbonRuntime"
    )
    public void setCarbonRuntime(CarbonRuntime carbonRuntime) {
        this.carbonRuntime = carbonRuntime;
        OSGiServiceHolder.getInstance().setCarbonRuntime(carbonRuntime);
    }

    /**
     * This is the unbind method for the above reference that gets called for carbonRuntime instance un-registrations.
     *
     * @param carbonRuntime the carbonRuntime instances that are un-registered.
     */
    public void unsetCarbonRuntime(CarbonRuntime carbonRuntime) {
        this.carbonRuntime = null;
        OSGiServiceHolder.getInstance().setCarbonRuntime(null);
    }
}
