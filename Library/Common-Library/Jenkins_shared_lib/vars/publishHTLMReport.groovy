#!/usr/bin/env groovy

def call(String allowMissing, String alwaysLinkToLastBuild, String keepAll, String reportDir, String reportFiles, String reportName) {
    publishHTML([allowMissing: "${allowMissing}", 
                alwaysLinkToLastBuild: "${alwaysLinkToLastBuild}", 
                keepAll: "${keepAll}",
                reportDir: "${reportDir}",
                reportFiles: "${reportFiles}",
                reportName: "${reportName}"])
}
