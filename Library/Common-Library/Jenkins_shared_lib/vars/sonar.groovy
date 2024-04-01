def call (String SONAR_PATH, String SONAR_APP_DIR){
    stage('SonarQube Analysis') {
                //timeout(time: 4, unit: 'MINUTES'){
                sh "cp $SONAR_PATH '$SONAR_APP_DIR'sonar-project.properties"
                // def scannerHome = tool 'SonarScanner';
                // withSonarQubeEnv([installationName: "sonar" ]) {
                //     sh "cd $SONAR_APP_DIR && $scannerHome/bin/sonar-scanner"
                // }
                withCredentials([string(credentialsId: 'sonar-key', variable: 'sonarid')]){
                sh "cd $SONAR_APP_DIR && /opt/sonar-scanner/bin/sonar-scanner -Dsonar.host.url=https://sonar.adanione.cloud/ -Dsonar.login=$sonarid"
                //}
            }
    }
}