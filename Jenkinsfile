pipeline {
    agent {
        docker {
            image 'markhobson/maven-node:latest'   // has Maven + Node + Git + Docker
            args '-v /var/run/docker.sock:/var/run/docker.sock'  // so docker-compose works
        }
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }

        stage('Start App with Docker Compose') {
            steps {
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        always {
            sh 'docker-compose down || true'
        }
    }
}
