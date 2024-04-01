#!/usr/bin/env groovy

def call(String subscription, String resourceGroup, String credentialsId, String apimName, String apiID, String imageTag, String urlSuffix, String serviceURL, String displayName, String swaggerURL ) {
        
    // sleep time: 60, unit: 'SECONDS'

    sh "rm -f swagger.json"
    sh 'sleep 5s'
    def download = sh(script: "curl -v ${swaggerURL} -o swagger.json", returnStatus: true)
    if (download == 0) {
        //compare both swaggers
        echo "Analysing Swaggers......"
        def diffExitCode = sh(script: "diff ${WORKSPACE}/swaggerOld.json ${WORKSPACE}/swagger.json", returnStatus: true)
        if (diffExitCode == 0) {
            //Do not update swagger
            echo "No Swagger changes found, Swagger Update not required."
            return false
        } else {
            //update swagger
            echo "Swagger changes Found, Updating Swagger..."
            
            withCredentials([azureServicePrincipal("${credentialsId}")]) {
                sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
            }
            sh "az account set --subscription ${subscription}"
            
            sh "az apim api import --path ${urlSuffix} --api-id ${apiID} --resource-group ${resourceGroup} -n ${apimName} --specification-path ${WORKSPACE}/swagger.json --specification-format openapi --service-url ${serviceURL}  --display-name ${displayName}"

            return true
            // sh "az storage blob upload --account-name azadldevemailimages --container-name swagger --name swagger:${imageTag}.json --source ${WORKSPACE}/swagger.json --connection-string $BLOB_SAS_DEV "
        }
    } else {
       echo "Couldn't download latest swagger, Please update manually."
       return false
    }
}








