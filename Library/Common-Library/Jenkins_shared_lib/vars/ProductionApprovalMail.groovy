#!/usr/bin/env groovy
def waitForInput(message, instructions) {
  timeout(time: 60, unit: 'MINUTES') {
    def userInput = null
    while (userInput == null) {
    //   userInput = input(id: 'emailApproval',message: message,parameters: [string(defaultValue: '', description: instructions, name: 'response')])
    userInput = input(id: 'emailApproval',message: message,parameters: [string(name: 'response', defaultValue: 'proceed', description: 'Description of the string parameter')])
    }
    return userInput
  }
}

def waitForEmailApproval() {
  def emailResponse = waitForInput('Deployment approval', 'Please reply to the email with "Proceed" or "Abort" to approve or reject the deployment.')

  // Parse the email response and take appropriate action
  if (emailResponse.trim().toLowerCase() == 'proceed') {
    echo 'Deployment approved. Proceeding...'
  } else {
    error 'Deployment rejected. Aborting...'
  }
}
def call(String recepient_mail){
        emailext mimeType: 'text/html',    
         body: '''<!DOCTYPE html>
<html>
<head>
  <style>
    /* Inline CSS styles */
    .container {
      max-width: 600px;
      margin: 0 auto;
      padding: 20px;
      font-family: Arial, sans-serif;
      background-color: #282c34;
      color: #001f3f; /* Change the text color to dark navy blue (#001f3f) */
    }
    .header {
      text-align: center;
      margin-bottom: 20px;
    }
    .header img {
      width: 150px;
    }
    .content {
      padding: 20px;
      background-color: #ffffff;
      border-radius: 5px;
      margin-top: 20px;
    }
    .button {
      display: inline-block;
      padding: 10px 20px;
      background-color: #ff9900; /* Amazon's button orange color */
      color: #ffffff; /* Button text color */
      text-decoration: none;
      border-radius: 5px;
      transition: background-color 0.3s ease;
    }
    .button:hover {
      background-color: #e68a00; /* Amazon's button hover color */
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">
      <img src="https://www.adanione.cloud/emailimages/Jenkins_logo.svg" alt="Jenkins Logo">
    </div>
    <div class="content">
      <h3>Dear Team,</h3>
      <p>Please review the build details and click the "Approve" button below:</p>
      <p><strong>Build Information:</strong></p>
      <ul>
        <li>Pipeline: <strong> \${JOB_NAME}</strong></li>
        <li>CR Number: <strong> \${CR_Number}</strong></li>
        <li>Build Enviornment: <strong>Production</strong></li>
        <li>Image Tag: <strong> \${IMAGE_TAG}</strong></li>
      </ul>
      <p>
        <a href="https://jenkinsadl.adanione.cloud/jenkins/job/\${JOB_NAME}/\${BUILD_NUMBER}/input" class="button">Approve</a>
      </p>
      <p><i>For any queries, feel free to reach us at <b>"ADL-DevOps@adani.com"</b></i></p>
      <p>Thank you!</p>
      <p><strong>ADL-DevOps</strong></p>
    </div>
  </div>
</body>
</html>
  ''',
  subject: 'Build Approval',
  to: "${recepient_mail}"
        waitForEmailApproval()
} 