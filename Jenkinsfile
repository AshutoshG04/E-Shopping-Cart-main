pipeline {
    agent any

    // Define tools to be pre-installed on the Jenkins agent
    // You must configure these in "Manage Jenkins" > "Tools"
    tools {
        jdk 'JDK 17'     // Or whatever you named your JDK 17
        nodejs 'Node 18' // Or whatever you named your Node.js
        maven 'Maven 3'  // Or whatever you named your Maven
    }

    environment {
        // Set environment variables
        CI = 'true'
        // Define the path to the single JAR we will build
        JAR_FILE = "backend/verto-shop/target/verto-shop-0.0.1-SNAPSHOT.jar"
    }

    stages {
        stage('Checkout') {
            steps {
                // Get the source code
                checkout scm
            }
        }

        stage('Build Frontend') {
            steps {
                // Navigate to the React app directory
                dir('src') {
                    sh 'node --version'
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Build & Test Backend') {
            steps {
                // 1. Prepare the static directory
                sh 'mkdir -p backend/verto-shop/src/main/resources/static'

                // 2. Copy the built React app into the Spring Boot app
                echo "Copying built frontend from 'src/dist' to 'backend/verto-shop/src/main/resources/static'"
                sh 'cp -r src/dist/* backend/verto-shop/src/main/resources/static/'

                // 3. Navigate to the backend directory and build the self-contained JAR
                // This will also run all of the backend's built-in tests
                dir('backend/verto-shop') {
                    sh './mvnw clean install'
                }
            }
        }

        stage('Run E2E Tests') {
            steps {
                // This block runs your 'Java/' test suite
                // It starts the server, runs tests, and ensures the server is stopped
                script {
                    def serverProc
                    try {
                        // 1. Start the newly built JAR file in the background
                        echo "Starting Spring Boot server: java -jar ${env.JAR_FILE}"
                        serverProc = sh(
                            script: "nohup java -jar ${env.JAR_FILE} > app.log 2>&1 & echo \$!",
                            returnStdout: true
                        ).trim()

                        echo "Server started with PID: ${serverProc}"

                        // 2. Wait for the server to be healthy (uses test config)
                        // This command points to your E2E test project
                        dir('Java') {
                            sh 'mvn clean test -Dsurefire.suiteXmlFiles=testng.xml'
                        }

                    } finally {
                        // 3. Always stop the server, even if tests fail
                        echo "Stopping server (PID: ${serverProc})..."
                        sh "kill -9 ${serverProc} || true"
                    }
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                echo "Archiving the final application JAR"
                archiveArtifacts artifacts: "${env.JAR_FILE}", followSymlinks: false
            }
        }
    }

    post {
        // Always run this, regardless of pipeline status
        always {
            // Clean up the workspace
            cleanWs()

            // Collect TestNG reports
            dir('Java') {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }

            // Collect backend test reports
            dir('backend/verto-shop') {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}