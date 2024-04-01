def call (String PROJECT_KEY, String APP_DIR){
    stage('Sonar Analysis') {
            withCredentials([string(credentialsId: 'sonar-key', variable: 'sonarid')]){
                sh "cd $APP_DIR && dotnet sonarscanner begin /k:$PROJECT_KEY /d:sonar.host.url=https://sonar.adanione.cloud/  /d:sonar.login=$sonarid && sudo dotnet build && dotnet sonarscanner end /d:sonar.login=$sonarid"
                sh "cd $APP_DIR && sudo dotnet build"
                sh "cd $APP_DIR && dotnet sonarscanner end /d:sonar.login=$sonarid"
        }
        }
}
