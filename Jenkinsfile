pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE = "viettranni/stockmaster3000"
        DOCKER_TAG = "latest1"
    }

    stages {
        stage('Install Buildx') {
            steps {
                script {
                    // Install Docker Buildx
                    if (isUnix()) {
                        sh '''
                            mkdir -vp ~/.docker/cli-plugins/
                            curl --silent -L "https://github.com/docker/buildx/releases/download/v0.3.0/buildx-v0.3.0.linux-amd64" -o ~/.docker/cli-plugins/docker-buildx
                            chmod a+x ~/.docker/cli-plugins/docker-buildx
                        '''
                    } else {
                        bat '''
                            mkdir -vp %USERPROFILE%\.docker\cli-plugins
                            curl --silent -L "https://github.com/docker/buildx/releases/download/v0.3.0/buildx-v0.3.0.windows-amd64" -o %USERPROFILE%\.docker\cli-plugins\docker-buildx
                            chmod a+x %USERPROFILE%\.docker\cli-plugins\docker-buildx
                        '''
                    }
                }
            }
        }

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
                git branch: 'viet2', url: 'https://github.com/SaedAbukar/StockMaster3000.git'
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

        stage('Test & Coverage') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'

                    discoverReferenceBuild()

                    recordCoverage(
                        tools: [[parser: 'JACOCO']],
                        id: 'jacoco',
                        name: 'JaCoCo Coverage',
                        sourceCodeRetention: 'EVERY_BUILD',
                        qualityGates: [
                            [threshold: 60.0, metric: 'LINE', baseline: 'PROJECT', unstable: true],
                            [threshold: 60.0, metric: 'BRANCH', baseline: 'PROJECT', unstable: true]
                        ]
                    )
                }
            }
        }

        stage('Set up Docker Buildx') {
            steps {
                script {
                    sh 'docker buildx create --use || true'
                    sh 'docker buildx inspect --bootstrap'
                }
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
                                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                                docker buildx build --platform linux/amd64,linux/arm64 \
                                    --build-arg OPENAI_API_KEY=$OPENAI_API_KEY \
                                    -t ${DOCKER_IMAGE}:${DOCKER_TAG} --push .
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
