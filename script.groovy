def testApp(){
  echo "Testing the Application"
  sh "python manage.py test"
}

def buildApp() { 
    echo "Building app with build number #${env.BUILD_NUMBER}"
   withCredentials([usernamePassword(credentialsId:"dockerhub-id", passwordVariable:"PASS", usernameVariable:"USER")]) {
        sh "docker build . -t waji97/test-ci:${env.BUILD_NUMBER}"
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh " docker push waji97:${env.BUILD_NUMBER}"
    }
}
