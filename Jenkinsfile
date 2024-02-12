node("jenkins-agent") {
    stage ('Test') {
        git branch: 'main', url: 'https://github.com/Waji-97/Test-CI-App.git'
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
        git branch: 'main', url: 'https://github.com/Waji-97/Test-CI-App.git'
        container('kaniko'){
            script {
                sh '''
                /kaniko/executor --dockerfile `pwd`/myproject/Dockerfile --context `pwd` --destination=waji97/test-ci:${BUILD_NUMBER}
                '''
            }
        }
    }

    stage ('Update IaC') {
        git branch: 'main', url: 'https://github.com/Waji-97/Test-CD-IaC.git'
        container('jenkins-agent'){
            withCredentials([usernamePassword(credentialsId: 'github-id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
                dir('Test-CD-IaC') {
                sh 'git config --global credential.helper "store --file ~/.git-credentials"'
                sh "echo 'https://$USERNAME:$PASSWORD@github.com' > ~/.git-credentials"
                sh 'sed -i "s|image: waji97/test-ci:.*|image: waji97/test-ci:${BUILD_NUMBER}|" deploy.yaml'
                sh 'git config user.email "wajiwos16@gmail.com"'
                sh 'git config user.name "Waji-97"'
                sh 'git add .'
                sh 'git commit -m "Update image tag to ${BUILD_NUMBER}"'
                sh 'git push origin main'
                }
            }
        }
    }
}
