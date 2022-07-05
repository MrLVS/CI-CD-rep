pipeline {
    stages {
        stage('Run Smoke tests') {
            parallel {
                stage('Run 2') {
                    steps {
                        sh "echo 'Env Name1 $NAME'"
                    }
                }
                stage('Run 2') {
                    steps {
                        sh "echo 'Env NAME2 = $NAME2'"
                        script{
                            RANAME2 = NAME2.tokenize('/')[-1]
                        }
                    }
                }
            }
        }
    }
}
