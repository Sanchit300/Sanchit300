#!/usr/bin/env groovy

def call(String repo_url, String branch, String dest_directory, String credentialsId, Boolean include_in_trigger=false) {
    def GIT_OUT=checkout changelog: "${include_in_trigger}", poll: "${include_in_trigger}", 
    scm: [$class: 'GitSCM', branches: [[name: "${branch}"]], 
    extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${dest_directory}"]], 
    userRemoteConfigs: [[credentialsId: "${credentialsId}", 
    url: "${repo_url}"]]]
    return GIT_OUT
}
