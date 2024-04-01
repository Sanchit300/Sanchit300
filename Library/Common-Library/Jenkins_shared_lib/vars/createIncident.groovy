#!/usr/bin/env groovy

def call(String snow_url, String username, String password, String payload) {
  def jsonResponse = sh(script: """curl -u "${username}":"${password}" -H "Content-Type: application/json" -X POST -d '${payload}' ${snow_url}""", returnStdout: true).trim()
  def jsonObj = readJSON text: jsonResponse
  sh "echo 'Incident Number: ${jsonObj.result[0].display_value}'"
  sh "echo 'Link to Incident: ${jsonObj.result[0].record_link}'"
  return "${jsonObj.result[0].sys_id}"
}