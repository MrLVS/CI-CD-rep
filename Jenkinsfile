pipeline {
    agent any
    triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'ref', value: '$.ref']
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
          branches: [[name: "*/${BRANCH}"]], 
          extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'srctest']], 
          userRemoteConfigs: [[
            credentialsId: 'Github', 
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