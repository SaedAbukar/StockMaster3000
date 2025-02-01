pipeline {
    agent any

    tools {
        maven 'Maven'  // This is the name you gave your Maven installation in Jenkins
    }

    environment {
        DOCKER_HOST = "npipe:////./pipe/docker_engine"  // Docker Desktop default pipe
        DOCKER_IMAGE = "saedabukar/stockmaster3000"  // Replace with your Docker Hub repository name
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

        stage('Build Docker Image') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker build -t "$DOCKER_IMAGE" .'
                    } else {
                        bat 'docker build -t "%DOCKER_IMAGE%" .'
                    }
                }
            }
        }

        stage('Test Docker Image') {  // New stage to test the Docker image
            steps {
                script {
                    echo "Testing Docker image..."
                    if (isUnix()) {
                        // Run the Docker image and check if it's working
                        sh 'docker run -d --name test-container "$DOCKER_IMAGE"'
                        sh 'docker ps -a'  // To verify the container is running
                        sh 'docker logs test-container'  // Check logs for any errors
                        sh 'docker stop test-container'  // Stop the container after test
                        sh 'docker rm test-container'    // Remove the container
                    } else {
                        bat 'docker run -d --name test-container "%DOCKER_IMAGE%"'
                        bat 'docker ps -a'  // Verify the container is running
                        bat 'docker logs test-container'  // Check logs
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

                    // Check if the environment is Unix or Windows
                    if (isUnix()) {
                        // Run Docker Compose for Unix-based systems (Linux/macOS)
                        sh 'docker-compose -f docker-compose.yml up -d'

                        // Verify if everything is running correctly
                        sh 'docker-compose ps'

                        // Optionally check logs to ensure everything started correctly
                        sh 'docker-compose logs'
                    } else {
                        // For Windows systems, use the Windows command to run Docker Compose
                        bat 'docker-compose -f docker-compose.yml up -d'

                        // Verify if everything is running correctly
                        bat 'docker-compose ps'

                        // Optionally check logs to ensure everything started correctly
                        bat 'docker-compose logs'
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    // Use docker.withRegistry() to push image to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        if (isUnix()) {
                            // Tag and push the Docker image
                            sh 'docker tag "$DOCKER_IMAGE" "$DOCKER_IMAGE:latest"'
                            sh 'docker push "$DOCKER_IMAGE:latest"'
                        } else {
                            bat 'docker tag "%DOCKER_IMAGE%" "%DOCKER_IMAGE%:latest"'
                            bat 'docker push "%DOCKER_IMAGE%:latest"'
                        }
                    }
                }
            }
        }
    }
}