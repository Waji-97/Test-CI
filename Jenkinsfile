node("any") {
    git branch: 'main', url: 'https://github.com/Waji-97/Test-CI-App.git'
    stage ('Test') {
        container('jenkins-agent'){
            dir('myproject') {
                sh 'python3 -m venv venv'
                sh '. venv/bin/activate'
                sh 'pip install -r requirements.txt --break-system-packages'
                sh 'python3 manage.py test'
            }
        }
    }

    stage ('Build & Push Docker Image') {
        container('kaniko'){
            script {
                sh '''
                /kaniko/executor --context `pwd` --dockerfile `pwd`/Dockerfile --destination=waji97/test-ci:v${env.BUILD_NUMBER}
                '''
            }
        }
    }

    stage ('Update IaC') {
        container('jenkins-agent'){
            checkout([$class: 'GitSCM',
                      branches: [[name: 'main']],
                      userRemoteConfigs: [[url: 'https://github.com/Waji-97/Test-CD-IaC.git']]])
            sh 'sed -i "s|image: waji97/test-ci:.*|image: waji97/test-ci:v${env.BUILD_NUMBER}|" deploy.yaml'
            sh 'git config user.email "wajiwos16@gmail.com"'
            sh 'git config user.name "Waji-97"'
            sh 'git add .'
            sh 'git commit -m "Update image tag to v${env.BUILD_NUMBER}"'
            sh 'git push'
        }
    }
}
