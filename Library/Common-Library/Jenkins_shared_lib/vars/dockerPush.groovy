#!/usr/bin/env groovy

def call(String credentialsId, String image_tag) {
  stage('Push Docker image to ACR') {
withCredentials([azureServicePrincipal("${credentialsId}")]) {
      sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
      sh "az acr login --name adlsuperappacr"
        }
    sh "docker push ${image_tag}"
    sh "docker rmi -f \"${image_tag}\""
  }
}