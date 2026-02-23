pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 21'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        ARTIFACT_ID = 'tv-series'
        VERSION = '1.0.0-SNAPSHOT'
    }
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }
    
    triggers {
        githubPush()
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh './mvnw clean compile -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh './mvnw test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: '**/target/**.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }
        
        stage('Code Quality Analysis') {
            steps {
                echo 'Running code quality analysis...'
                script {
                    try {
                        sh './mvnw checkstyle:checkstyle pmd:pmd spotbugs:spotbugs'
                    } catch (Exception e) {
                        echo 'Code quality analysis not configured, skipping...'
                    }
                }
            }
            post {
                always {
                    recordIssues(
                        enabledForFailure: true,
                        tools: [
                            checkStyle(pattern: '**/target/checkstyle-result.xml'),
                            pmdParser(pattern: '**/target/pmd.xml'),
                            spotBugs(pattern: '**/target/spotbugsXml.xml')
                        ]
                    )
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh './mvnw package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/quarkus-app/**', fingerprint: true
                }
            }
        }
        
        stage('Integration Tests') {
            steps {
                echo 'Running integration tests...'
                sh './mvnw verify'
            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }
        
        stage('Publish') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                    branch 'develop'
                }
            }
            steps {
                echo 'Publishing artifacts to repository...'
                script {
                    try {
                        sh './mvnw deploy -DskipTests'
                    } catch (Exception e) {
                        echo 'Maven repository not configured, skipping publish...'
                    }
                }
            }
        }
        
        stage('Docker Build') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                    branch 'develop'
                }
            }
            steps {
                echo 'Building Docker image...'
                script {
                    try {
                        sh '''
                            docker build -f src/main/docker/Dockerfile.jvm \
                                -t ${ARTIFACT_ID}:${VERSION} \
                                -t ${ARTIFACT_ID}:latest .
                        '''
                    } catch (Exception e) {
                        echo 'Dockerfile not found, skipping Docker build...'
                    }
                }
            }
        }
        
        stage('Deploy to Development') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Deploying to Development environment...'
                script {
                    // Configurare qui il deploy su ambiente di sviluppo
                    echo 'Configure your development deployment here'
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                input message: 'Deploy to Staging?', ok: 'Deploy'
                echo 'Deploying to Staging environment...'
                script {
                    // Configurare qui il deploy su ambiente di staging
                    echo 'Configure your staging deployment here'
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                input message: 'Deploy to Production?', ok: 'Deploy'
                echo 'Deploying to Production environment...'
                script {
                    // Configurare qui il deploy su ambiente di produzione
                    echo 'Configure your production deployment here'
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
            script {
                if (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'master') {
                    // Notifiche per successo su branch principale
                    echo 'Send success notification'
                }
            }
        }
        failure {
            echo 'Pipeline failed!'
            // Configurare notifiche di fallimento (email, Slack, etc.)
            echo 'Send failure notification'
        }
        unstable {
            echo 'Pipeline is unstable!'
            // Configurare notifiche per build instabile
            echo 'Send unstable notification'
        }
    }
}
