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
        [key: 'action', value: '$.action'],
        [key: 'pull_request_number', value: '$.pull_request.number'],
        [key: 'target_branch', value: '$.pull_request.base.ref'],
     ],
     token: 'unittests',
     causeString: 'Build started because pull request №$pull_request_number has status $action in target branch $target_branch',
     printContributedVariables: true,
     printPostContent: true,
     regexpFilterText: '$action $target_branch'
     regexpFilterExpression: '^opened main$|^reopened main$|^synchronize main$'
    )
  }
    stages {
        stage('Helow world') {
            steps {
                sh "echo 'Default branch $target_branch'"
                sh "echo 'PR number = $pull_request_number'"


            }
        }
        stage("Checkout"){
      steps {
        checkout([$class: 'GitSCM', 
          branches: [[name: "*/$CM_BRANCH"]], 
          extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'srctest']], 
          userRemoteConfigs: [[
            credentialsId: '9d0f1888-1c7c-44b2-ac22-59f2e511e86d', 
            url: 'git@github.com:MrLVS/Kubernetes.git'
          ]]
        ])
        sh "echo $BRANCH_SEV ----------------------"
      }
    }
     stage("Checkout sev"){
      steps {
        checkout([$class: 'GitSCM', 
          branches: [[name: "*/$BRANCH_SEV"]], 
          extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'srcDocker']], 
          userRemoteConfigs: [[
            credentialsId: '9d0f1888-1c7c-44b2-ac22-59f2e511e86d', 
            url: 'git@github.com:MrLVS/Docker.git'
          ]]
        ])
      }
    }
    stage('Check worckspace') {
      steps {
        // Rewrite config cores to solr, add a certificate for smtp validataor to work
        sh """
        ls -A ${WORKSPACE}
        echo "---------------------------------------------------"
        ls -A ${WORKSPACE}/srctest
        echo "---------------------------------------------------"
        ls -A ${WORKSPACE}/srcDocker
        """
      }
    }
    }
}