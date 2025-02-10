pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE = "ibudaa/stockmaster3000"
        DOCKER_TAG = "latest"
    }

    stages {
        stage('Set Docker Host') {
            steps {
                script {
                    if (isUnix()) {
                        env.DOCKER_HOST = 'unix:///var/run/docker.sock'
                    } else {
                        env.DOCKER_HOST = 'npipe:////./pipe/docker_engine'
                    }
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
                script {
                    if (isUnix()) {
                        sh 'mvn clean package -Pproduction -DskipTests'
                    } else {
                        bat 'mvn clean package -Pproduction -DskipTests'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Enable Docker Buildx') {  // ✅ Moved up before Docker build
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker buildx create --use'
                    } else {
                        bat 'docker buildx create --use'
                    }
                }
            }
        }

        stage('Build & Push Multi-Arch Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials'){
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials'){
                        if (isUnix()) {
                            sh '''
                            docker buildx build --platform linux/amd64,linux/arm64 \
                                -t $DOCKER_IMAGE:$DOCKER_TAG --push .
                            '''
                        } else {
                            bat '''
                            docker buildx build --platform linux/amd64,linux/arm64 ^
                                -t %DOCKER_IMAGE%:%DOCKER_TAG% --push .
                            '''
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
                    if (isUnix()) {
                        sh '''
                        docker-compose -f docker-compose.yml down
                        docker-compose -f docker-compose.yml up -d
                        docker-compose ps
                        docker-compose logs
                        '''
                    } else {
                        bat '''
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

    post {
        success {
            echo 'Build and deployment completed successfully!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
    }
    }
}