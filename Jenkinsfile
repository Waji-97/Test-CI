pipeline {
    agent any

    environment {
        DOCKER_REGISTRY_CREDENTIALS = 'dockerhub-id'
        DOCKER_IMAGE_NAME = 'waji97/test-ci'
    }

    stages {
        stage('Test') {
            steps {
                // Run Django tests
                dir('myproject') {
                    sh 'python manage.py test'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    docker.build "${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}"

                    // Push the Docker image to Docker Hub
                    docker.withRegistry('https://hub.docker.com', DOCKER_REGISTRY_CREDENTIALS) {
                        docker.image("${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }

        stage('Update Infrastructure Code') {
            steps {
                // Checkout the infrastructure code repository
                checkout([$class: 'GitSCM', 
                          branches: [[name: 'main']], 
                          userRemoteConfigs: [[url: 'https://github.com/Waji-97/Test-CD-IaC.git']]])

                // Update the image tag in the infrastructure code
                sh 'sed -i "s|image: waji97/test-ci:.*|image: waji97/test-ci:${env.BUILD_NUMBER}|" deploy.yaml'

                // Commit and push changes
                sh 'git config user.email "wajiwos16@gmail.com"'
                sh 'git config user.name "Waji-97"'
                sh 'git add .'
                sh 'git commit -m "Update image tag to ${env.BUILD_NUMBER}"'
                sh 'git push'
            }
        }
    }
}
