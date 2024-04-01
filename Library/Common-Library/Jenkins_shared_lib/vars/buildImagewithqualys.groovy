#!/usr/bin/env groovy

def call(String app, String image_tag, String dockerfile_path, String context) {
  stage('Build Docker image') {
        try{
            dockerImage = docker.build("${image_tag}", "-f ${dockerfile_path} ${context}")
            //Test created image
            //Wait for container
        }
        catch(Exception e){
            error("Test container is not responding as expected: "+e.toString())
        }
  stage('Extract Image Id') {
        if (params.Qualys as boolean) {
            if ("${image_tag}".contains("adlsuperappacr")) {
                    echo "Skipping Qualys check as Dev environment"
            } else {
                def IMAGE_ID = sh(script: "docker inspect -f '{{.Id}}' " + "${image_tag}", returnStdout: true).trim()
                getImageVulnsFromQualys imageIds: IMAGE_ID, useGlobalConfig: true
            }
        }
    }      
}
}

