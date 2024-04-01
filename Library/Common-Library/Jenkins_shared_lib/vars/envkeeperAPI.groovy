#!/usr/bin/env groovy

def call(String envName, String language, String lob, String type, String vaultName, String secretName, String swaggerURL, String crNumber='-' ) {
    stage('Jenkins Analytics API') {
        def jobName = env.JOB_NAME
        def buildNumber = env.BUILD_NUMBER
        def status = (currentBuild.result == null) ? 'FAILURE' : currentBuild.result
        def duration = currentBuild.duration
        def timestamp = env.BUILD_TIMESTAMP
        def imageTag = params.IMAGE_TAG
        def user = (currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause').userName[0] == null) ? params.buildUser : currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause').userName[0]
        def kubernetesClusterVersion = sh(script: "kubectl version --output=json | jq -r '.serverVersion.gitVersion'",returnStdout: true).trim().split('\n')
        def fetchVersion = sh(script: "az keyvault secret show --name ${secretName} --vault-name ${vaultName} --query 'id' --output tsv", returnStdout: true).trim()
        def arr = fetchVersion.split('/')
        def version = arr[-1]
        def apiURL = (envName == "PROD-DR") ? "https://aksdev.adanione.cloud/api/deployment-dashboard/jenkins/jobs" : "https://deployment-dashboard-dev.adanione.cloud/jenkins/jobs"
        
        sh """
        curl --location --request POST '${apiURL}' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "jobName": "${jobName}",
                "buildNumber": "${buildNumber}",
                "status": "${status}",
                "duration": "${duration}",
                "timestamp": "${timestamp}",
                "triggeredBy": "${user}",
                "env": "${envName}",
                "language": "${language}",
                "imageTag": "${imageTag}",
                "lob": "${lob}",
                "kubernetesClusterVersion": "${kubernetesClusterVersion[0]}",
                "type": "${type}",
                "secretName":"${secretName}",
                "secretVersion":"${version}",
                "keyVaultName": "${vaultName}",
                "swaggerFileURL": "${swaggerURL}",
                "cr": "${crNumber}"
            }'
        """
    }
}
