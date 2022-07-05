pipeline {
    agent any
    stages {
        stage('Run  tests') {
            parallel {
                stage('Run 1') {
                    steps {
                        sh "echo 'Env Name1 $NAME'"
                    }
                }
                stage('Run 2') {
                    steps {
                        
                        script{
                            NAME2 = NAME2.tokenize('/')[-1]
                        }
                        sh "echo 'Env NAME2 = $NAME2'"
                    }
                }
            }
        }
    }
}