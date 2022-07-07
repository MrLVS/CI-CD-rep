pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
               sh """ echo '$TEST_TYPE $JOB_KILLING_TIMEOUT $PUSH_TO_TESTRAIL $TESTS_PART $IMAGE_TAG'"""
            }
        }
    }
}
