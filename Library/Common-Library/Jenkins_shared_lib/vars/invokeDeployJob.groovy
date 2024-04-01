#!/usr/bin/env groovy

def call(String job_name, String image_tag) {
  stage ('Invoke deployment job') {
        build job: "${job_name}", parameters: [
          [$class: "StringParameterValue", name: "IMAGE_TAG", value: "${image_tag}"]
            ]
    }
}
