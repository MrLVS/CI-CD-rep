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
     regexpFilterExpression: 'refs/heads/' + BRANCH_NAME
    )
  }
    stages {
        stage('Helow world') {
            steps {
                sh "echo 'Hello from CI-CD with change from $ref'"
            }
        }
    }
}