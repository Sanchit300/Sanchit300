#!/usr/bin/env groovy

def call(String app, String application_path, Boolean run_unit_tests, String unit_test_path="NA") {
  def build_image=docker.build("build-agent-${app}:${BUILD_NUMBER}","--network=host -f ./cicd/Dev-AKS/Airport-API/Dockerfiles/Dockerfile-api-build-agent .").inside("--network=host -u root"){
        stage('Build Application'){
            sh "dotnet publish -c Release ${application_path}"
        }
        if("${run_unit_tests}"=="true" && "${unit_test_path}" != "NA"){
          stage('Run unit tests')
          {
            sh "dotnet test --logger \"html;logfilename=testResults.html\" ${unit_test_path}"
            publishHTMLReport('true', 'true', 'true', "${unit_test_path}/TestResults/", 'testResults.html', 'Unit test report')
          }
        }
  }
  sh "docker rmi -f build-agent-${app}:${BUILD_NUMBER}"
}
