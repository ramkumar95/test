pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {

        stage("cleanup workspace") {
           steps { 
           cleanWs()
           }
        }

        stage("checkout SCM") {
           steps { 
           git branch: 'main', poll: false, url: 'https://github.com/ramkumar95/test.git'
           }
        }

        stage("build") {
           steps { 
           sh 'mvn clean package -e'
           }
        }

        stage("quality test analysis") {
           steps { 
             script{
              withSonarQubeEnv(credentialsId: 'sonarqube') {
              sh "mvn sonar:sonar"
             }
           }
           }
        }
       
        stage("quality gate") {
           steps { 
            script { 
           waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube'
           }
           }
        }

    }
}