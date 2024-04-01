def call (String SONAR_PATH, String SONAR_APP_DIR){
    stage('SonarQube Analysis') {
{
                sh "cp $SONAR_PATH '$SONAR_APP_DIR'sonar-project.properties"
                def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation';
                withSonarQubeEnv('Sonarqube') {
                sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey='superapp-dutyfree-api' -Dsonar.sources='/var/lib/jenkins/workspace/'"
                //withSonarQubeEnv([installationName: "sonar" ]) {
                    //sh "cd $SONAR_APP_DIR && $scannerHome/bin/sonar-scanner"
                //}
                }
            }
}
