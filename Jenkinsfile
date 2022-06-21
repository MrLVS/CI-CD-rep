pipeline {
    agent any
    parameters{
    //Note: you can also use choice parameters instead of string here.
    string(name: 'SEV_BRANCH', defaultValue: 'master', description: 'Branch from smtp-email-validator repo')
    string(name: 'TESTS_JOB_NAME', defaultValue: 'cm-smoke-tests', description: 'The name of the job to be launched by the trigger')
    string(name: 'PUSH_TO_TESTRAIL', defaultValue: 'yes', description: 'Parameter to push results tests in testrail') 
    }
    triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'ref', value: '$.ref'],
      [key: 'action', value: '$.action']
     ],
     token: 'unittests',
     causeString: 'Triggered from branch $ref',
     printContributedVariables: true,
     printPostContent: true,
     regexpFilterText: '$ref',
     regexpFilterExpression: '^(refs/heads/test)$'
    )
  }
    stages {
        stage('Helow world') {
            steps {
                sh "echo 'Hello from CI-CD with change from $ref'"

            }
        }
        stage("Checkout"){
      steps {
        checkout([$class: 'GitSCM', 
          branches: [[name: "$ref"]], 
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