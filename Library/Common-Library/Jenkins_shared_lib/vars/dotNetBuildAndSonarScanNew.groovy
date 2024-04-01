#!/usr/bin/env groovy

def call(String app, String sonar_instance, String application_path, String sonarqube_project_key, String exclusions, Boolean run_unit_tests, String unit_test_path="NA") {
  def build_image=docker.build("build-agent-${app}:${BUILD_NUMBER}","--network=host -f ./cicd/Dev-AKS/Airport-API/Dockerfiles/Dockerfile-api-build-agent .").inside("--network=host -u root"){
        
        withSonarQubeEnv([installationName: "${sonar_instance}" ]) {
            if("${run_unit_tests}"=="true" && "${unit_test_path}" != "NA"){
                stage('Build app and SonarQube Analysis with unit tests'){
                    sh """dotnet sonarscanner begin /k:"${sonarqube_project_key}" /d:sonar.cs.vscoveragexml.reportsPaths=coverage.xml /d:sonar.exclusions="${exclusions}"
                        dotnet publish -c Release ${application_path}
                        dotnet-coverage collect 'dotnet test  --logger "html;logfilename=testResults.html" ${unit_test_path}' -f xml  -o 'coverage.xml'
                        dotnet sonarscanner end
                    """
                    publishHTMLReport('true', 'true', 'true', "${unit_test_path}/TestResults/", 'testResults.html', 'Unit test report')

                }
            }
            else{
                stage('Build app and SonarQube Analysis'){
                    sh """
                    dotnet sonarscanner begin /k:"${sonarqube_project_key}" /d:sonar.exclusions="${exclusions}"
                    dotnet publish -c Release ${application_path}
                    dotnet sonarscanner end 
                    """
                }
            }
        }
        
        stage('Quality Gate'){
            timeout (time: 5, unit: 'MINUTES'){
                def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        }//Quality Gate stage closed
    }//Docker agent closed
    //Clean up
    sh "docker rmi -f build-agent-${app}:${BUILD_NUMBER}"
}
