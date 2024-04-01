#!/usr/bin/env groovy

def call(String subscription, String resourceGroup, String credentialsId, String apimName, String apiID, String appDir, String urlSuffix, String serviceURL, String displayName ) {
    stage('Updating Swagger') {
        // authenticating to service principle
        withCredentials([azureServicePrincipal("${credentialsId}")]) {
            sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
        }
        // Authenticate to AKS cluster
        sh "az account set --subscription ${subscription}"

        //sh "az apim api update -g ${resourceGroup} -n ${apimName} --api-id ${apiID} --set specificationFormat=openapi specification-path ${appDir}"
        sh "az apim api import --path ${urlSuffix} --api-id ${apiID} --resource-group ${resourceGroup} -n ${apimName} --specification-path ${appDir}/swagger.json --specification-format openapi --service-url ${serviceURL}  --display-name ${displayName}"
    }
}