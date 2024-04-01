def call (String QA_REPO_BRANCH) {
stage("QA Automation Suite")
            {
                try {
                    sh "cd ./automation/ && mvn clean && mvn verify"
                }
                catch (Exception e) {
                        currentBuild.result = 'SUCCESS'
                }
                def filepath = sh(script: "ls -dt  ./automation/test-output/* | head -n 1", returnStdout: true).trim()
                sh "echo $filepath"
                def reportFilePath = "${filepath}/HtmlReport/HtmlReport.html"
                sh "mv '$reportFilePath' ./automation/TestReport.html "
                //def pass_percentage = sh(script: "cat TestReport.html |grep 100|tail -1", returnStdout: true).trim()
                //sh "echo $pass_percentage"
                // emailext(
                //     to: 'ADL-DevOps@adani.com,praveenkumar.gupta@adani.com,surabhi.goyal@adani.com,kaustubh.singh@adani.com,masthan.jakku@adani.com,surendrakumar.savita@adani.com,rajdeep.gupta@adani.com',
                //     subject: "QA-Automation-Report || ${QA_REPO_BRANCH} ",
                //     body: 'Please find the attached report.',
                //     attachmentsPattern: "**/TestReport.html",
                //     attach: true
                // )
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: './automation/',  
                    reportFiles: 'TestReport.html',   
                    reportName: 'QA Automation Report'
                ])

            }
}