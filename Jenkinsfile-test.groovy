pipeline {
    agent any
    parameters{
        string(name: 'SEV_BRANCH', defaultValue: 'master', description: 'Branch from smtp-email-validator repo')
        string(name: 'TESTS_JOB_NAME', defaultValue: '', description: 'The name of the job to be launched by the trigger')
        string(name: 'PUSH_TO_TESTRAIL', defaultValue: 'yes', description: 'Parameter to push test results in testrail')
        string(name: 'TEST_TYPE', defaultValue: 'smoke', description: 'Type of test to run')
        string(name: 'SPECIFIED_TESTS_LIST', defaultValue: '', description: 'Test lists')
    }
        stages {
            stage('Build images'){
                steps{
                sh 'echo "Build images"'
            }}
            stage('Run  acc or smoke tests') {
                when{
                    expression{ env.TEST_TYPE == 'acceptance' || env.TEST_TYPE == 'smoke' && !env.SPECIFIED_TESTS_LIST }
                }
                    steps {
                        sh "echo 'Env TEST_TYPE $TEST_TYPE list - $SPECIFIED_TESTS_LIST'"
                } 
            }
            stage('Run  SPECIFIED_TESTS_LIST') {
                when{ expression{ env.SPECIFIED_TESTS_LIST }}
                
                    steps {
                        sh "echo 'Env TEST_TYPE $TEST_TYPE list - $SPECIFIED_TESTS_LIST'"
                } 
            }
            stage('Run  unit tests') {
                when{ expression{ !env.SPECIFIED_TESTS_LIST  && env.TEST_TYPE == 'unit'}}
                
                    steps {
                        sh "echo 'Env TEST_TYPE $TEST_TYPE list - $SPECIFIED_TESTS_LIST'"
                        currentStage.result = 'FAILURE'
                } 
            }
        }
}


