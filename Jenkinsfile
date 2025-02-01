pipeline {
    agent any

    tools {
        maven 'Maven'  // This is the name you gave your Maven installation in Jenkins
    }

    environment {
        // Set Docker host for Unix-based systems (Linux/macOS)
        DOCKER_HOST = (isUnix()) ? 'unix:///var/run/docker.sock' : 'npipe:////./pipe/docker_engine' // For Windows

        DOCKER_IMAGE = "saedabukar/stockmaster3000"  // Replace with your Docker Hub repository name
        DOCKER_TAG = "latest" // Optionally, you can use a dynamic tag based on the commit ID or branch name
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Pawaffle/StockMaster3000.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "Building the application using Maven..."
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
                    echo "Running tests on the application..."
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    if (isUnix()) {
                        sh 'docker build -t "$DOCKER_IMAGE:$DOCKER_TAG" .'
                    } else {
                        bat 'docker build -t "%DOCKER_IMAGE%:%DOCKER_TAG%" .'
                    }
                }
            }
        }

        stage('Test Docker Image') {
            steps {
                script {
                    echo "Testing the Docker image..."
                    if (isUnix()) {
                        sh 'docker run -d --name test-container "$DOCKER_IMAGE:$DOCKER_TAG"'
                        sh 'docker ps -a'
                        sh 'docker logs test-container'
                        sh 'docker stop test-container'
                        sh 'docker rm test-container'
                    } else {
                        bat 'docker run -d --name test-container "%DOCKER_IMAGE%:%DOCKER_TAG%"'
                        bat 'docker ps -a'
                        bat 'docker logs test-container'
                        bat 'docker stop test-container'
                        bat 'docker rm test-container'
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    echo "Deploying with Docker Compose..."

                    // Ensure you're using the correct directory for the docker-compose.yml file
                    dir('.') {
                        if (isUnix()) {
                            sh 'docker-compose -f docker-compose.yml up -d'
                            sh 'docker-compose ps'
                            sh 'docker-compose logs'
                        } else {
                            bat 'docker-compose -f docker-compose.yml up -d'
                            bat 'docker-compose ps'
                            bat 'docker-compose logs'
                        }
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    echo "Pushing Docker image to Docker Hub..."
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        if (isUnix()) {
                            sh 'docker tag "$DOCKER_IMAGE:$DOCKER_TAG" "$DOCKER_IMAGE:$DOCKER_TAG"'
                            sh 'docker push "$DOCKER_IMAGE:$DOCKER_TAG"'
                        } else {
                            bat 'docker tag "%DOCKER_IMAGE%:%DOCKER_TAG%" "%DOCKER_IMAGE%:%DOCKER_TAG%"'
                            bat 'docker push "%DOCKER_IMAGE%:%DOCKER_TAG%"'
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment completed successfully!'
            // Add notifications for successful build (e.g., email, Slack)
        }
        failure {
            echo 'Build or deployment failed!'
            // Add notifications for failed builds
        }
    }
}
