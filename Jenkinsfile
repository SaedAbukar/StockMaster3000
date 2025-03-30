pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "viettranni/stockmaster3000"
        DOCKER_TAG = "latest1"
    }

    stages {
        stage('Set Docker Context') {
            steps {
                sh 'docker context use unix:///var/run/docker.sock'
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

        stage('Build & Push Multi-Arch Image') {
            steps {
                script {
                    withCredentials([
                        string(credentialsId: 'openai-api-key-id', variable: 'OPENAI_API_KEY'),
                        usernamePassword(credentialsId: 'viettranni', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                    ]) {
                        docker.withRegistry('https://index.docker.io/v1/', 'viettranni') {
                            sh """
                                docker login -u "$DOCKER_USER" --password-stdin <<< "$DOCKER_PASS"
                                docker buildx build --platform linux/amd64,linux/arm64 \
                                    --build-arg OPENAI_API_KEY=$OPENAI_API_KEY \
                                    -t ${DOCKER_IMAGE}:${DOCKER_TAG} --push .
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
