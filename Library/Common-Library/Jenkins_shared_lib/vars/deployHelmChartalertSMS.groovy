#!/usr/bin/env groovy

def call(String credentialsId, String subscriptionId, String resourceGroup, String clusterName, String project, String app, String chart_path, String values_file, String image_tag, String cert, String key, String env, String ingress_values_file, String appsettings_secret, String gcp_secret_project, String namespace) {
    stage('Deploy to AKS') {
        // def (image_name, tag_number)="${image_tag}".tokenize( ':' )
        // def build_image=docker.build("deploy-agent-${app}:${BUILD_NUMBER}","--network=host -f ./cicd/az-supperapp-cicd/Dockerfile-api-deploy-agent .").inside("--network=host -u root"){
        // authenticating to service principle
        withCredentials([azureServicePrincipal("${credentialsId}")]) {
            sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
        }
        //Authenticate to AKS cluster
        sh "az account set --subscription ${subscriptionId}"

        sh "az aks get-credentials --resource-group ${resourceGroup} --name ${clusterName} --overwrite-existing"
                // Create TLS in K8s secrets
                // sh "gcloud secrets versions access \"latest\" --secret=${cert} --project adl-sa-secret-project > tmp.crt"
                // sh "gcloud secrets versions access \"latest\" --secret=${key} --project adl-sa-secret-project > tmp.key"
                // try{
                //     def response=sh(script: "kubectl get secret adl-${env}-cert --no-headers -o name --ignore-not-found", returnStdout: true).trim()
                //     if(response.contains("secret/adl-${env}-cert")){
                //         echo "Secret already exists"
                //     }
                //     else{
                //         sh "kubectl create secret tls adl-${env}-cert --cert tmp.crt --key tmp.key"
                //     }
                // }
                // catch (Exception e) {
                //     echo "Issue while creating K8s secret"
                // }
                // finally{
                //     sh "rm tmp.crt && rm tmp.key"
                // }
        // def $encoded_appsettings=sh(script: "gcloud secrets versions access \"latest\" --secret=${appsettings_secret} --project=${gcp_secret_project} | base64", returnStdout: true).trim()
        // sh "echo 'secret:' > temp_values.yaml"
        // sh "echo -n '  encoded_appsettings: "+$encoded_appsettings.replaceAll("[\n\r]", "")+"' >> temp_values.yaml"
        // sh "helm upgrade --install \"${namespace}-release\" ./cicd/az-supperapp-cicd/Helm/adl-sa-api-namespace/ --namespace=${namespace}"
        sh "helm upgrade --install \"${app}-api-release\" ./cicd/Wilmar/Helm/adl-sa-api-helm-chart/ --values=${values_file} --set deployment.image=${image_tag} --namespace=${namespace}"
        // if("${app}".contains("sampling")){
        // sh "helm upgrade --install sampling-api-ingress ./cicd/az-supperapp-cicd/Helm/adl-sa-api-common/ --values=${ingress_values_file}"                
        // }
        // else{
        // sh "helm upgrade --install common-api-ingress ./cicd/az-supperapp-cicd/Helm/adl-sa-api-common/ --values=${ingress_values_file}"                                    
        // }
        //sh "kubelogin convert-kubeconfig -l azurecli"
        // sh "helm upgrade --install \"${app}-api-release\" ${chart_path} --values=${values_file} --values=/home/ayush_shukla_adani_com/sampling-admin-api.yaml --set deployment.image=${image_tag} --namespace=${namespace} --set secret.name=${app}-appsettings.json"
        
        // }
        sh "docker rmi -f deploy-agent-${app}:${BUILD_NUMBER}"

    }
}
