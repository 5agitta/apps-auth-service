pipeline {
    agent any

    stages {
        stage ('Build Image') {
            steps {
                checkout scmGit(branches: [[name: '*/dev']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-sagitta-credentials', url: 'https://github.com/5agitta/apps-auth-service.git']])

                script {
                    sh "java -version"
                    sh "./gradlew bootBuildImage --info"
                }
            }
        }

        stage ('Push Docker Image') {
            steps {
                script {
                    def username = sh(
                        script: './gradlew properties | grep -w "registryUsername" | awk -F ": " \'{print $2}\' | awk \'{$1=$1};1\' | tr -d "\\n"',
                        returnStdout: true,
                    )

                    def projectName = sh(
                        script: './gradlew properties | grep -w "name" | awk -F ": " \'{print $2}\' | awk \'{$1=$1};1\' | tr -d "\\n"',
                        returnStdout: true,
                    )

                    def version = sh(
                        script: './gradlew properties | grep -w "version" | awk -F ": " \'{print $2}\' | awk \'{$1=$1};1\' | tr -d "\\n"',
                        returnStdout: true,
                    )

                    // Push the Docker image to the registry
                    sh "docker push $username/$projectName:$version"
                }
            }
        }
    }
}