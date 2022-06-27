pipeline {
    agent any
    parameters{
    //Note: you can also use choice parameters instead of string here.
    string(name: 'SEV_BRANCH', defaultValue: 'main', description: 'Branch from smtp-email-validator repo')
    string(name: 'CM_BRANCH', defaultValue: 'main', description: 'Branch from campaign_management repo')
    string(name: 'TESTS_JOB_NAME', defaultValue: 'cm-smoke-tests', description: 'The name of the job to be launched by the trigger')
    string(name: 'PUSH_TO_TESTRAIL', defaultValue: 'yes', description: 'Parameter to push results tests in testrail') 
    }
    triggers {
    GenericTrigger(
     genericVariables: [
        [key: 'ACTION', value: '$.action'],
        [key: 'PULL_REQUEST_NUMBER', value: '$.pull_request.number'],
        [key: 'TARGET_BRANCH', value: '$.pull_request.base.ref'],
        [key: 'PULL_REQUEST_BRANCH', value: '$.pull_request.head.ref'],
        [key: 'SHA_COMMIT', value: '$.pull_request.head.sha']
     ],
     token: 'unittests',
     tokenCredentialId: 'webhooktext',
     causeString: 'Build started because pull request №$PULL_REQUEST_NUMBER has status $ACTION in target branch $TARGET_BRANCH',
     printContributedVariables: true,
     printPostContent: true,
     regexpFilterText: '$ACTION $TARGET_BRANCH',
     regexpFilterExpression: '^opened main$|^reopened main$|^synchronize main$'
    )
  }
  stages {
    stage("Echo env vars"){
      steps{
        sh "env"
      }
    }
        stage('Helow world') {
                environment {
                MESSAGE_COMMIT = SHA_COMMIT.take(30)
                SHA_COMMIT_DISPLAY = SHA_COMMIT.take(7)
             }
            steps {
                sh "echo 'Branch pull request $TARGET_BRANCH'"
                sh "echo 'PR number = $PULL_REQUEST_NUMBER'"
                sh "echo 'SHA_COMMIT = $SHA_COMMIT'"
                script{
                  currentBuild.displayName = "#${BUILD_NUMBER}-PR#${MESSAGE_COMMIT}-${SHA_COMMIT_DISPLAY}"
                  
                  echo "TEST_VARIABLE = ${env.PULL_REQUEST_BRANCH}"
                }


            }
        }
      stage("Checkout"){
      steps {
        checkout([$class: 'GitSCM', 
          branches: [[name: "*/$PULL_REQUEST_BRANCH"]], 
          extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'srctest']], 
          userRemoteConfigs: [[
            credentialsId: '9d0f1888-1c7c-44b2-ac22-59f2e511e86d', 
            url: 'git@github.com:MrLVS/Kubernetes.git'
          ]]
        ])
        sh "echo $PULL_REQUEST_BRANCH ----------------------"
      }
    }
     stage("Checkout sev"){
      steps {
        checkout([$class: 'GitSCM', 
          branches: [[name: "*/$SEV_BRANCH"]], 
          extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'srcDocker']], 
          userRemoteConfigs: [[
            credentialsId: '9d0f1888-1c7c-44b2-ac22-59f2e511e86d', 
            url: 'git@github.com:MrLVS/Docker.git'
          ]]
        ])
      }
    }
    stage('get logs') {
      steps {
        sh "mkdir -p ${WORKSPACE}/logs && cp -r /var/lib/jenkins/jreport.xml ${WORKSPACE}/test-reports/junit-report.xml"

        junit '**/test-reports/*.xml'
  
      }
    }
  }
  post {
    always {
        archiveArtifacts artifacts: "test-report.xml"
        cleanWs()
    }
  }
}