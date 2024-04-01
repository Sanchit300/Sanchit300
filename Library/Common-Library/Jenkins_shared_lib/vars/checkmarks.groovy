def call(String CHECKMARX_JOB_NAME, String CHECKMARKS_PROJECT_NAME) {
if (params.Checkmarx as boolean){

    stage("Checkmarx SAST Scan"){
                 step([$class: 'CxScanBuilder', comment: '', configAsCode: true, credentialsId: '', customFields: '', excludeFolders: 'cicd', exclusionsSetting: 'job', failBuildOnNewResults: false, failBuildOnNewSeverity: 'HIGH', filterPattern: '''!**/_cvs/**/*, !**/.svn/**/*, !**/.hg/**/*, !**/.git/**/*, !**/.bzr/**/*,
                !**/.gitgnore/**/*, !**/.gradle/**/*, !**/.checkstyle/**/*, !**/.classpath/**/*, !**/bin/**/*,
                !**/obj/**/*, !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr, !**/*.iws,
                !**/*.bak, !**/*.tmp, !**/*.aac, !**/*.aif, !**/*.iff, !**/*.m3u, !**/*.mid, !**/*.mp3,
                !**/*.mpa, !**/*.ra, !**/*.wav, !**/*.wma, !**/*.3g2, !**/*.3gp, !**/*.asf, !**/*.asx,
                !**/*.avi, !**/*.flv, !**/*.mov, !**/*.mp4, !**/*.mpg, !**/*.rm, !**/*.swf, !**/*.vob,
                !**/*.wmv, !**/*.bmp, !**/*.gif, !**/*.jpg, !**/*.png, !**/*.psd, !**/*.tif, !**/*.swf,
                !**/*.jar, !**/*.zip, !**/*.rar, !**/*.exe, !**/*.dll, !**/*.pdb, !**/*.7z, !**/*.gz,
                !**/*.tar.gz, !**/*.tar, !**/*.gz, !**/*.ahtm, !**/*.ahtml, !**/*.fhtml, !**/*.hdm,
                !**/*.hdml, !**/*.hsql, !**/*.ht, !**/*.hta, !**/*.htc, !**/*.htd, !**/*.war, !**/*.ear,
                !**/*.htmls, !**/*.ihtml, !**/*.mht, !**/*.mhtm, !**/*.mhtml, !**/*.ssi, !**/*.stm,
                !**/*.bin,!**/*.lock,!**/*.svg,!**/*.obj, 
                !**/*.stml, !**/*.ttml, !**/*.txn, !**/*.xhtm, !**/*.xhtml, !**/*.class, !**/*.iml, !Checkmarx/Reports/*.*,
                !OSADependencies.json, !**/node_modules/**/*, !**/.cxsca-results.json, !**/.cxsca-sast-results.json, !.checkmarx/cx.config''', fullScanCycle: 10, fullScansScheduled: true, groupId: '1', incremental:  false, jobStatusOnError: 'UNSTABLE', password: '', preset: '1', projectName: "${CHECKMARKS_PROJECT_NAME}", sastEnabled: true, serverUrl: 'https://checkmarx.adanione.cloud/', sourceEncoding: '1', username: '', vulnerabilityThresholdResult: 'FAILURE', comment: "${CHECKMARX_JOB_NAME}"])
            }

    stage("Checkmarx SCA Scan"){
         step([$class: 'CxScanBuilder', comment: '', configAsCode: true, credentialsId: '', customFields: '', dependencyScanConfig: [dependencyScanExcludeFolders: '', dependencyScanPatterns: '', enableScaResolver: 'MANIFEST', fsaVariables: '', osaArchiveIncludePatterns: '*.zip, *.war, *.ear, *.tgz', pathToScaResolver: '', sastCredentialsId: '', scaAccessControlUrl: 'https://platform.checkmarx.net', scaConfigFile: '', scaCredentialsId: '', scaEnvVariables: '', scaResolverAddParameters: '', scaSASTProjectFullPath: '', scaSASTProjectID: '', scaSastServerUrl: '', scaServerUrl: 'https://api-sca.checkmarx.net', scaTeamPath: '', scaTenant: '', scaWebAppUrl: 'https://sca.checkmarx.net'], excludeFolders: 'cicd', exclusionsSetting: 'job', failBuildOnNewResults: false, failBuildOnNewSeverity: 'HIGH', filterPattern: '''!**/_cvs/**/*, !**/.svn/**/*, !**/.hg/**/*, !**/.git/**/*, !**/.bzr/**/*,
                !**/.gitgnore/**/*, !**/.gradle/**/*, !**/.checkstyle/**/*, !**/.classpath/**/*, !**/bin/**/*,
                !**/obj/**/*, !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr, !**/*.iws,
                !**/*.bak, !**/*.tmp, !**/*.aac, !**/*.aif, !**/*.iff, !**/*.m3u, !**/*.mid, !**/*.mp3,
                !**/*.mpa, !**/*.ra, !**/*.wav, !**/*.wma, !**/*.3g2, !**/*.3gp, !**/*.asf, !**/*.asx,
                !**/*.avi, !**/*.flv, !**/*.mov, !**/*.mp4, !**/*.mpg, !**/*.rm, !**/*.swf, !**/*.vob,
                !**/*.wmv, !**/*.bmp, !**/*.gif, !**/*.jpg, !**/*.png, !**/*.psd, !**/*.tif, !**/*.swf,
                !**/*.jar, !**/*.zip, !**/*.rar, !**/*.exe, !**/*.dll, !**/*.pdb, !**/*.7z, !**/*.gz,
                !**/*.tar.gz, !**/*.tar, !**/*.gz, !**/*.ahtm, !**/*.ahtml, !**/*.fhtml, !**/*.hdm,
                !**/*.hdml, !**/*.hsql, !**/*.ht, !**/*.hta, !**/*.htc, !**/*.htd, !**/*.war, !**/*.ear,
                !**/*.htmls, !**/*.ihtml, !**/*.mht, !**/*.mhtm, !**/*.mhtml, !**/*.ssi, !**/*.stm,
                !**/*.bin,!**/*.lock,!**/*.svg,!**/*.obj,
                !**/*.stml, !**/*.ttml, !**/*.txn, !**/*.xhtm, !**/*.xhtml, !**/*.class, !**/*.iml, !Checkmarx/Reports/*.*,
                !OSADependencies.json, !**/node_modules/**/*, !**/.cxsca-results.json, !**/.cxsca-sast-results.json, !.checkmarx/cx.config''', fullScanCycle: 10, fullScansScheduled: true, groupId: '1', incremental: true, jobStatusOnError: 'UNSTABLE', password: '', preset: '1', projectName: "${CHECKMARX_JOB_NAME}", scaReportFormat: 'PDF', serverUrl: 'https://checkmarx.adanione.cloud/', sourceEncoding: '1', username: '', vulnerabilityThresholdResult: 'FAILURE', comment: "${CHECKMARX_JOB_NAME}"])
            }
}
}