#!/usr/bin/env groovy

def call(String subscription, String resourceGroup, String credentialsId, String apimName, String apiID, String appDir, String urlSuffix, String serviceURL, String displayName, String swaggerURL ) {
    sleep time: 60, unit: 'SECONDS'
    // authenticating to service principle
    withCredentials([azureServicePrincipal("${credentialsId}")]) {
        sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
    }
    // Authenticate to AKS cluster
    sh "az account set --subscription ${subscription}"
    sh "rm -f swagger.json"
    def download = sh(script: "curl -v ${swaggerURL} -o swagger.json", returnStatus: true)
    if (download == 0) {
        echo "Swagger file Downloaded Successfully, Updating the Swagger..."
        sh "az apim api import --path ${urlSuffix} --api-id ${apiID} --resource-group ${resourceGroup} -n ${apimName} --specification-path ${appDir} --specification-format openapi --service-url ${serviceURL}  --display-name ${displayName}"
    } else {
       echo "Couldn't download swagger, please update manually."
    }
}