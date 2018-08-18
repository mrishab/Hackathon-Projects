# Introduction
This sample demonstrates how to use service template JAX-RS framework and security library to enable the platform agnostic authentication

# Setup 
## Setup in CF
1. Make sure the following prerequisite is setup already in your cloudfoundry space 
   * AppRouter
   * UAA Instance for AppRouter
   * Test tenant is already created 
2. Build project via `mvn clean install -Dtomcat`
3. Edit *manifest.yml* and change the following property 
   * Update `bocpoc-uaa` with service name of UAA instance used by the AppRouter
   * Update `hostname` and `name` with `samplejaxrs-<your i number>`
4. Run `cf push` to deploy your service   
Now you should be able to access the service under `https://<tennat_id>-<approuter url>/samplejaxrs-<your i number>/health`

## Setup in Neo
As register service requires quite a lot steps, so you can simply use the existing *samplejaxrs* service deployed in Neo Canary. Url: https://account.int.sap.hana.ondemand.com/cockpit#/acc/wb059a960/app/samplejaxrs/dashboard . 

To update it, you can
1. Build project via `mvn clean install`
2. Logon Neo Cockpit to update the service
Now you should be able to access the service under
`https://<tenant>-sap-com.cnry.projectorca.cloud/samplejaxrs/health`

# Endpoint  
Service exposes the endpoins as below 

* */api/v1/model*   
This endpoint demonstrates the request to HANA through App2AppSSO 
   * `/api/v1/model`    
   It will return the list of the model 

   * `/api/v1/model/async`   
   It demonstrates how to implement REST in async mode 

   * `/api/v1/mode/<id>`    
   It will return the model for given id

   * `POST /api/v1/model` 
   It will create the model 





