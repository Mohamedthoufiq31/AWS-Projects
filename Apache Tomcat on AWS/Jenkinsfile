
pipeline {
    agent {
        label 'tomcat-agent'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = 'tomcat-practice-1.0'
        TOMCAT_HOME = '/opt/apache-tomcat-9.0.122'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out Java application from GitHub'
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Building application using Maven'
                sh 'mvn -B clean verify'
            }
        }

        stage('Verify WAR') {
            steps {
                sh '''
                    test -f target/${APP_NAME}.war
                    ls -lh target/${APP_NAME}.war
                '''
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying WAR to Apache Tomcat'

                sh '''
                    test -d "$TOMCAT_HOME/webapps"

                    cp "target/${APP_NAME}.war" \
                       "$TOMCAT_HOME/webapps/${APP_NAME}.war"

                    ls -lh "$TOMCAT_HOME/webapps/${APP_NAME}.war"
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Checking application endpoint'

                sh '''
                    curl --fail --silent --show-error \
                         --retry 10 \
                         --retry-delay 3 \
                         --retry-connrefused \
                         http://localhost:9090/${APP_NAME}/hello
                '''
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Application built, tested and deployed!'
            echo 'URL: http://54.221.60.120:9090/tomcat-practice-1.0/hello'
        }

        failure {
            echo 'FAILED: Check the Jenkins Console Output for errors.'
        }

        always {
            archiveArtifacts(
                artifacts: 'target/*.war',
                allowEmptyArchive: true,
                fingerprint: true
            )
        }
    }
}
