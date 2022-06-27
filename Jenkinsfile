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
    //  tokenCredentialId: 'app-secret',
     causeString: 'Build started because pull request №$PULL_REQUEST_NUMBER has status $ACTION in target branch $TARGET_BRANCH',
     printContributedVariables: true,
     printPostContent: true,
     regexpFilterText: '$ACTION $TARGET_BRANCH',
     regexpFilterExpression: '^opened main$|^reopened main$|^synchronize main$'
    )
  }
  stages {
   stage('Check run') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'jenkins-junit-reporter',
                                          usernameVariable: 'GITHUB_APP',
                                          passwordVariable: 'GITHUB_ACCESS_TOKEN')]) {
            sh '''
            curl -H "Content-Type: application/json" \
                 -H "Accept: application/vnd.github.antiope-preview+json" \
                 -H "authorization: Bearer ${GITHUB_ACCESS_TOKEN}" \
                 -d '{ "name": "check_run", \
                       "head_sha": "'${GIT_COMMIT}'", \
                       "status": "in_progress", \
                       "external_id": "42", \
                       "started_at": "2020-03-05T11:14:52Z", \
                       "output": { "title": "Check run from Jenkins!", \
                                   "summary": "This is a check run which has been generated from Jenkins as GitHub App", \
                                   "text": "...and that is awesome"}}' https://api.github.com/repos/<org>/<repo>/check-runs
            '''
        }
      }
   }
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
        sh "mkdir -p ${WORKSPACE}/build/test-reports && cp -r /var/lib/jenkins/jreport.xml ${WORKSPACE}/build/test-reports/junit-report.xml"
  
      }
    }
    stage('tests') {
      steps{
        junit testResults: '**/test-reports/*.xml'
      }
    }
  }
  post {
    always {
        archiveArtifacts artifacts: "build/test-reports/*.xml"
        cleanWs()
    }
  }
}