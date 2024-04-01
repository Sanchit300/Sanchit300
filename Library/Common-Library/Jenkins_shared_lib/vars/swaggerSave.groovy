#!/usr/bin/env groovy
def call(String environment, String storageAccount, String containerName, String blobSubscriptionId, String imageTag, String crNumber = "NA" ) {
    
    def jobName = env.JOB_NAME
    def buildNumber = env.BUILD_NUMBER
    def swaggerFileName
    def blobSAS
    def swaggerBlobURL

    if(environment == "PRODUCTION"){
        swaggerFileName = "production/swagger-${jobName}:${buildNumber}-${imageTag.split('/')[-1]}:${crNumber}.json"
        blobSAS = 'BLOB_SAS_PROD'
        swaggerBlobURL = "https://azadlprodgrafanalokilogs.blob.core.windows.net/swagger/${swaggerFileName}"
        sh "az account set --subscription ${blobSubscriptionId}"
    }else if(environment == "STAGE"){
        swaggerFileName = "stage/swagger-${jobName}:${buildNumber}-${imageTag.split('/')[-1]}.json"
        blobSAS = 'BLOB_SAS_DEV'
        swaggerBlobURL = "https://azadldevemailimagesa.blob.core.windows.net/swagger/${swaggerFileName}"
        sh "az account set --subscription ${blobSubscriptionId}"
    }

    echo "Saving Swagger..."
    withCredentials([string(credentialsId: blobSAS, variable: 'BLOB_SAS')]) {
        def swaggerUpload = sh(script: "az storage blob upload --account-name ${storageAccount} --container-name ${containerName} --name ${swaggerFileName}  --file ${WORKSPACE}/swagger.json --sas-token \"$BLOB_SAS\"", returnStatus: true)
        if (swaggerUpload == 0) {
            echo "Swagger upload was successful"
            return swaggerBlobURL
        } else {
            echo "...some error occured during swagger saving"
            return "false"
        }
    }
}
