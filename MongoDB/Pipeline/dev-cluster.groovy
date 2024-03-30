@Library('jenkins-new-common-library')_
node('linux-agent13') {
    def $CICD_REPO = "git@github.com:AdaniDigitalLabs/ADL-AKS-DevOps-CI-CD.git"
    def $CICD_REPO_BRANCH = "mongo-helm"
    def $CICD_REPO_DEST = "cicd"
    def $GIT_CREDS="jenkins-private-key"
    def $VALUES_FILE="./cicd/MongoDB/Helm/Values/dev-values.yaml"
    def $CHART_PATH="./cicd/MongoDB/Helm/Mongo"
    def $CHART_PATH_EXPRESS="./cicd/MongoDB/Helm/Express"
    def $VALUES_FILE_EXPRESS="./cicd/MongoDB/Helm/Values/dev-express-values.yaml"

    try {
        stage('Checkout'){
            gitCheckout($CICD_REPO, $CICD_REPO_BRANCH, $CICD_REPO_DEST, $GIT_CREDS)
            properties = readProperties file: './cicd/MongoDB/dev-cluster.properties'
        }

        stage('Cluster Login'){

            withCredentials([azureServicePrincipal("${properties.credentialsId}")]) {
                sh "az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID"
            }

            sh "az account set --subscription ${properties.subscriptionId}"

            sh "az aks get-credentials --resource-group ${properties.resourceGroup} --name ${properties.clusterName} --overwrite-existing"
        
        }

        stage('Fetching Secrets'){

            def rootUname = sh(script: "az keyvault secret show --name KV-MONGO-ROOT-USERNAME --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()
            def rootPwd = sh(script: "az keyvault secret show --name KV-MONGO-ROOT-PASSWORD --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()
            def uname = sh(script: "az keyvault secret show --name KV-MONGO-USERNAME --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()
            def pwd = sh(script: "az keyvault secret show --name KV-MONGO-PASSWORD --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()
            def list = sh(script: "az keyvault secret show --name KV-MONGO-USERS-LIST --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()
            def expressUser = sh(script: "az keyvault secret show --name KV-MONGO-EXPRESS-USER --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()
            def expressPass = sh(script: "az keyvault secret show --name KV-MONGO-EXPRESS-PASSWORD --vault-name az-adl-sa-devqa-keyvault --query \"value\" --output tsv", returnStdout: true).trim()

            sh """
                set +x
                
                mkfifo MONGO_ROOT_PASSWORD  MONGO_ROOT_USERNAME  MONGO_USERNAME  MONGO_PASSWORD  MONGO_USERS_LIST  MONGO_EXPRESS_USER  MONGO_EXPRESS_PASSWORD

                echo \'${rootUname}\' > MONGO_ROOT_USERNAME &
                echo \'${rootPwd}\' > MONGO_ROOT_PASSWORD &
                echo \'${uname}\' > MONGO_USERNAME &
                echo \'${pwd}\' > MONGO_PASSWORD &
                echo \'${list}\' > MONGO_USERS_LIST &
                echo \'${expressUser}\' > MONGO_EXPRESS_USER &
                echo \'${expressPass}\' > MONGO_EXPRESS_PASSWORD &
                
                kubectl create secret generic mongodb-secret --from-file=MONGO_ROOT_PASSWORD --from-file=MONGO_ROOT_USERNAME  --from-file=MONGO_USERNAME --from-file=MONGO_PASSWORD --from-file=MONGO_USERS_LIST --from-file=MONGO_EXPRESS_USER --from-file=MONGO_EXPRESS_PASSWORD -n ${properties.MONGO_NAMESPACE}

                rm -rf MONGO_ROOT_PASSWORD
                rm -rf MONGO_ROOT_USERNAME
                rm -rf MONGO_USERNAME
                rm -rf MONGO_PASSWORD
                rm -rf MONGO_USERS_LIST
                rm -rf MONGO_EXPRESS_USER
                rm -rf MONGO_EXPRESS_PASSWORD
            """
        }

        stage('Installing MongoDB'){
            sh "helm upgrade --install \"mongo-database-release\" ${$CHART_PATH} --values=${$VALUES_FILE}  --namespace=${properties.MONGO_NAMESPACE} "
        }

        stage('Installing Mongo-Express'){
            sh "helm upgrade --install \"mongo-express-release\" ${$CHART_PATH_EXPRESS} --values=${$VALUES_FILE_EXPRESS}  --namespace=${properties.MONGO_NAMESPACE} "
        }

        currentBuild.result = 'SUCCESS'
    }

    catch (e){
        echo "Some error(s) occured during execution!"
        def logContent = Jenkins.getInstance()
            .getItemByFullName(env.JOB_NAME)
            .getBuildByNumber(
                Integer.parseInt(env.BUILD_NUMBER))
            .logFile.text
        // copy the log in the job's own workspace
        writeFile file: "build.log", text: logContent
        sh "echo 'Build failed due to:' >> build.log"
        sh "echo '${e}' >> build.log"
        
        currentBuild.result = 'FAILURE'
        throw e
    }
    finally{
        sh """
            rm -f MONGO_ROOT_PASSWORD
            rm -f MONGO_ROOT_USERNAME
            rm -f MONGO_USERNAME
            rm -f MONGO_PASSWORD
            rm -f MONGO_USERS_LIST
            rm -rf MONGO_EXPRESS_USER
            rm -rf MONGO_EXPRESS_PASSWORD
        """
    }
}
