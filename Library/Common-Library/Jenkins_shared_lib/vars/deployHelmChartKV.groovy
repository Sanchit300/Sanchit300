#!/usr/bin/env groovy

def call(String credentialsId, String subscriptionId, String resourceGroup, String clusterName, String app, String chart_path, String values_file, String release, String image_tag, String namespace) {
    stage('Deploy to AKS') {
        // authenticating to service principle
        withCredentials([azureServicePrincipal("${credentialsId}")]) {
            sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
        }
        //Authenticate to GKE cluster
        sh "az account set --subscription ${subscriptionId}"

        // Create TLS in K8s secrets
        sh "az aks get-credentials --resource-group ${resourceGroup} --name ${clusterName} --overwrite-existing"

        //sh "kubelogin convert-kubeconfig -l azurecli"
        
        sh "helm upgrade --install \"${app}-api-release\" ${chart_path} --values=${values_file}  --set deployment.image=${image_tag} --namespace=${namespace} --set namespace.name=${namespace} --set keyvaultVersion=${release}"
    }
}
