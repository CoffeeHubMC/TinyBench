pipeline {
    agent {
        label 'orange'
    }

    environment {
        LANG = 'en_US.UTF-8'
        LANGUAGE = 'en_US.UTF-8'
        LC_ALL = 'en_US.UTF-8'
    }

    stages {
        stage('Environment') {
            steps {
                script {
                    sh script: 'ls -a', label: 'Workspace'
                    sh script: 'chmod +x gradlew && ./gradlew --version', label: 'Gradle'
                    sh script: 'java -version', label: 'Java'
                    sh script: 'git --version', label: 'Git'
                }
            }
        }
        stage('Build') {
            steps {
                withCredentials([
                    usernamePassword(
                    credentialsId: 'coffeehubNexusAuth',
                    usernameVariable: 'ORG_GRADLE_PROJECT_coffeehubUsername',
                    passwordVariable: 'ORG_GRADLE_PROJECT_coffeehubPassword'),
                    string(credentialsId: 'coffeehubNexusURL', variable: 'ORG_GRADLE_PROJECT_nexusURL'),
                    string(credentialsId: 'devCoffeehubNexusURL', variable: 'ORG_GRADLE_PROJECT_devNexusURL')
                ]) {
                    sh script: 'chmod +x gradlew && ./gradlew clean build shadowJar publish', label: 'Build project, make shadow jar and publish it'
                    archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}