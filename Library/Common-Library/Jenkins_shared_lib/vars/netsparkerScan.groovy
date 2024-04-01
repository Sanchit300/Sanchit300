#!/usr/bin/env groovy

def call(String websiteID, String profileID, String severity, String scanType, String ignoreFalsePositive, String ignoreRiskAccepted) {
    NCScanBuilder credentialsId: '', ncDoNotFail: false, ncIgnoreFalsePositive: "${ignoreFalsePositive}", ncIgnoreRiskAccepted: "${ignoreRiskAccepted}", ncProfileId: "${profileID}", ncScanType: "${scanType}", ncSeverity: "${severity}", ncStopScan: true, ncWebsiteId: "${websiteID}"
}