pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "viettranni/stockmaster3000"
        DOCKER_TAG = "latest1"
    }

    stages {
        stage('Set Docker Context') {
            steps {
                script {
                    env.DOCKER_HOST = 'unix:///var/run/docker.sock'
                }
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'viet2', url: 'https://github.com/SaedAbukar/StockMaster3000.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -Pproduction -DskipTests'
            }
        }



        stage('Build & Push Image') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(credentialsId: 'viettranni', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                    ]) {
                        docker.withRegistry('https://index.docker.io/v1/', 'viettranni') {
                            sh """
                                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                                export DOCKER_BUILDKIT=0
                                docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                                docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment completed successfully!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
    }
}
