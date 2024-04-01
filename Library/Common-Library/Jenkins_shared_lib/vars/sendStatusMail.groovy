#!/usr/bin/env groovy

def call(String recepient_mail, String commit_id, String app_version) {
    
    emailext mimeType: 'text/html',
        body: """<!DOCTYPE html>
                <html>
                <head>
                </head>
                <body>
                    <h3>Project: """+env.JOB_NAME+"""</h3>
                    <h3>Build URL: """+env.BUILD_URL+""" </h3>
                    <p>App version: ${app_version}</p>
                    <p>Commit ID: ${commit_id}</p>
                    <h4>Refer attached log files.</h4>
                </body>
                </html>
            """,
        subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}", 
        to: "${recepient_mail}",
        attachLog: true,
        compressLog: true
}