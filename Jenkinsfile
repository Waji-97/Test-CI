def gv
pipeline { 

    agent any
    stages { 
        stage('Init') { 
            steps { 
                script { 
                    gv = load "script.groovy"
                }
            }
        }
        stage ('TestApp') { 
            steps  { 
                script {
                    gv.testApp()
                }
            }
        }
        stage ('BuildApp'){
            steps{
                script{
                   gv.buildApp()
                }
            }
        }
    }
}
