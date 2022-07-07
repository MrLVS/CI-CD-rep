def allPartsTests = [ PART_1 : 'AllTestPart1',
                      PART_2 : 'AllTestPart2',
                      PART_3 : 'AllTestPart3',
                      PART_4 : 'AllTestPart4',
                      PART_5 : 'AllTestPart5',
                      PART_6 : 'AllTestPart6',
                      PART_7 : 'AllTestPart7',
                      PART_8 : 'AllTestPart8']

pipeline {
    agent any
    parameters{
        string(name: 'SEV_BRANCH', defaultValue: 'master', description: 'Branch from smtp-email-validator repo')
        string(name: 'TESTS_JOB_NAME', defaultValue: 'child-job', description: 'The name of the job to be launched by the trigger')
        string(name: 'PUSH_TO_TESTRAIL', defaultValue: 'yes', description: 'Parameter to push test results in testrail')
        string(name: 'TEST_TYPE', defaultValue: 'smoke', description: 'Type of test to run')
        string(name: 'SPECIFIED_TESTS_LIST', defaultValue: '', description: 'Test lists')
    }
    environment {
            IMAGE_TAG = "${JOB_NAME}-${BUILD_NUMBER}" 

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
                sh " echo '$TESTS_PARTS'"
                script {
                    def allJobs = [:]
                    for(key in allPartsTests.keySet()){
                        if(key in TESTS_PARTS){
                            def jobName = IMAGE_TAG + "-" + key
                            println (allPartsTests[key])
                            allJobs[jobName] =  {
                                                build (job: TESTS_JOB_NAME, parameters:
                                                [
                                                string(name: 'IMAGE_TAG', value: IMAGE_TAG),
                                                string(name: 'TESTS_PART', value: allPartsTests[key]),
                                                string(name: 'JOB_KILLING_TIMEOUT', value: JOB_KILLING_TIMEOUT),
                                                string(name: 'PUSH_TO_TESTRAIL', value: PUSH_TO_TESTRAIL),
                                                string(name: 'TEST_TYPE', value: TEST_TYPE)
                                                ])
                                            }

                        }
                    }
                    parallel allJobs


                        // for(key in allPartsTests.keySet()) {
                        //     def jobName = "IMAGE_TAG" + curJob
                        //     allJobs[jobName] =  {
                        //     build (job: "main_job",
                        //             parameters:
                        //             [string(name:"PARAM", value:jobName)])
                        //     }
                        // }
                        // 
                }   
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
                        
                    } 
            }
        }
}


