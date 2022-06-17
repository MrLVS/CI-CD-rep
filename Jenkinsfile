pipeline {
    agent any
    parameters{
    //Note: you can also use choice parameters instead of string here.
    string(name: 'BRANCH_CM', defaultValue: 'master', description: 'branch from kuber repo') 
    }
    triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'ref', value: '$.ref'],
      [ key: 'BRANCH_CM', value: 'test' ],
     ],
     token: 'smoketests',
     causeString: 'Triggered on $ref',
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
          branches: [[name: "*/${BRANCH_CM}"]], 
          extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'srctest']], 
          userRemoteConfigs: [[
            credentialsId: '9d0f1888-1c7c-44b2-ac22-59f2e511e86d', 
            url: 'git@github.com:MrLVS/Kubernetes.git'
          ]]
        ])
      }
    }
    stage('Check worckspace') {
      steps {
        // Rewrite config cores to solr, add a certificate for smtp validataor to work
        sh """
        ls -A ${WORKSPACE}
        ls -A ${WORKSPACE}/srctest

        """
      }
    }
    }
}