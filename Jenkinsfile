pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "ibudaa/stockmaster3000"
        DOCKER_TAG = "latest"
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
                git branch: 'ivan', url: 'https://github.com/SaedAbukar/StockMaster3000.git'
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
                        string(credentialsId: 'openai-api-key-id', variable: 'OPENAI_API_KEY'),
                        usernamePassword(credentialsId: 'viettranni', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                    ]) {
                        docker.withRegistry('https://index.docker.io/v1/', 'viettranni') {
                            sh """
                                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                                export DOCKER_BUILDKIT=0
                                docker build --build-arg OPENAI_API_KEY=${OPENAI_API_KEY} -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                                docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                            """
                        }
                    }
                }
            }
        }

        stage('Test Docker Image') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            docker run -d --name test-container "$DOCKER_IMAGE:$DOCKER_TAG"
                            docker ps -a
                            docker logs test-container
                            docker stop test-container
                            docker rm test-container
                        '''
                    } else {
                        bat '''
                            docker run -d --name test-container "%DOCKER_IMAGE%:%DOCKER_TAG%"
                            docker ps -a
                            docker logs test-container
                            docker stop test-container
                            docker rm test-container
                        '''
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'openai-api-key-id', variable: 'OPENAI_API_KEY')]) {
                        if (isUnix()) {
                            sh '''
                                echo "OPENAI_API_KEY=$OPENAI_API_KEY" > .env
                                docker-compose -f docker-compose.yml down
                                docker-compose -f docker-compose.yml up -d
                                docker-compose ps
                                docker-compose logs
                            '''
                        } else {
                            bat '''
                                echo OPENAI_API_KEY=%OPENAI_API_KEY% > .env
                                docker-compose -f docker-compose.yml down
                                docker-compose -f docker-compose.yml up -d
                                docker-compose ps
                                docker-compose logs
                            '''
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
