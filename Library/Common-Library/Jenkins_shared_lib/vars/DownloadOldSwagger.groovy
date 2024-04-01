def call(String swaggerURL ) {
    sh "rm -f swaggerOld.json"
    //fetching old swagger
    def download = sh(script: "curl -v ${swaggerURL} -o swaggerOld.json", returnStatus: true)
    if (download == 0) {
        echo "Current Swagger file Downloaded Successfully"
    } else {
       echo "Couldn't download swagger, please update manually."
    }

}