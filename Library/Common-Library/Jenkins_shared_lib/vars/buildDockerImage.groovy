#!/usr/bin/env groovy

def call(String app, String image_tag, String dockerfile_path, String context) {
  stage('Build Docker image') {
        try{

            dockerImage = docker.build("${image_tag}", "-f ${dockerfile_path} --build-arg BASEIMAGE=${params.BASEIMAGE} ${context}")


            //Test created image
            // if("${app}".contains("sampling")){
            //     dockerImage.run("-d -p ${test_port}:80 -e ASPNETCORE_ENVIRONMENT=Staging --name test-container-${app}-$BUILD_NUMBER")
            // }
            // else{
            //     dockerImage.run("-d -p ${test_port}:80 --name test-container-${app}-$BUILD_NUMBER")
            // }
            //Wait for container
            // sh 'sleep 3s'
            // def response=sh(script: "curl -I ${test_url}", returnStdout: true).trim()
            // echo "${response}"
            // if(response.contains("HTTP/1.1 200 OK")){
            //     echo "Status OK!"
            // }
            // else{
            //     currentBuild.result = 'FAILURE'
            //     error("Test container did not respond with HTTP 200")
            // }
        }
        catch(Exception e){
            // error("Test container is not responding as expected: "+e.toString())
        }
        finally{
            // sh "docker stop \"test-container-$app-$BUILD_NUMBER\""
            // sh "docker rm \"test-container-$app-$BUILD_NUMBER\""
        }
    }//Build and push Docker image
}
