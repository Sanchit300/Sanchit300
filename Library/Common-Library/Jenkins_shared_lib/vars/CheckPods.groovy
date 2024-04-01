#!/usr/bin/env groovy

def call(String namespace, String deploymentName, String labelName, String targetTimestamp, String subscription, String credentialsId) {
    
    def desiredReplicas = sh(script: "kubectl get hpa -n $namespace -o json | jq -r --arg DEPLOYMENT \"$deploymentName\"  '.items[] | select(.spec.scaleTargetRef.name == \"$deploymentName\") | .spec.minReplicas'", returnStdout: true).trim().toInteger()
    def HEALTHY_PODS = 0
    def check = false
    sleep time: 10, unit: 'SECONDS'
    def PODS = sh(script: "kubectl get pods -l app=$labelName -n $namespace -o json | jq -r '.items[] | select(.metadata.creationTimestamp > \"$targetTimestamp\") | .metadata.name'", returnStdout: true).trim().split('\n')
    echo PODS.toString()
    if (PODS[0].isEmpty()) {
        echo "No New Deployments Found"
        return check
    } else {
        // def timeout = 30 * desiredReplicas // Set a timeout of 60 seconds (1 minutes)
        def timeout = (desiredReplicas == 1) ? 90 : (30 * desiredReplicas)
        def startTime = System.currentTimeMillis()
        
        while (HEALTHY_PODS < desiredReplicas) {
            def currentPods = sh(script: "kubectl get pods -l app=$labelName -n $namespace -o json | jq -r '.items[] | select(.metadata.creationTimestamp > \"$targetTimestamp\" and .status.containerStatuses[0].ready == true and .status.containerStatuses[0].restartCount == 0) | .metadata.name'", returnStdout: true).trim().split('\n')
            echo currentPods.toString()
            
            if (!currentPods[0].isEmpty()) {
                check = true
                echo currentPods.length.toString()
                HEALTHY_PODS = currentPods.length
                echo "Healthy Pods: $HEALTHY_PODS out of $desiredReplicas Pods"
            } else {
                echo "Still fetching Healthy Pods, Retrying..."
                
                // Check if the timeout has been reached
                def currentTime = System.currentTimeMillis()
                def elapsedTime = (currentTime - startTime) / 1000
                if (elapsedTime >= timeout) {
                    echo "Timeout reached. No healthy pods after $timeout seconds."
                    break // Exit the loop if the timeout is reached
                }
            }
        }
        return check
    }
}
