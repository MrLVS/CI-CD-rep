pipeline {
    agent any
    stages {
        stage('Run  tests') {
            environment{
                NAME2 = "${NAME2.tokenize('/')[-1]}"

            }
            parallel {
                stage('Run 1') {
                    steps {
                        sh "echo 'Env Name1 $NAME'"
                    }
                }
                stage('Run 2') {
                    steps {
                        sh "echo 'Env NAME_CM = $NAME2'"
                    }
                }
                stage('Run 3') {
                    steps {
                        
                        sh "echo 'Env NAME_CM = $NAME2'"
                    }
                }
            }
        }
        stage('Run  tests2') {
                                steps {
                        
                        sh "echo 'Env NAME_CM = $NAME2'"
                    }
        }
    }
}
