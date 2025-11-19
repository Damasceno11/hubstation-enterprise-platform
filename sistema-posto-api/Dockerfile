# Etapa 1: Define a imagem base (Java 17 Alpine - Leve e Seguro)
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Cria um usuário não-root para segurança (Prática Enterprise)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Argumento para localizar o arquivo .jar gerado pelo Maven
ARG JAR_FILE=target/*.jar

# Copia o jar da sua máquina para dentro do container renomeando para app.jar
COPY ${JAR_FILE} app.jar

# Expõe a porta 8080 (apenas documentação, o docker-compose é quem libera)
EXPOSE 8080

# Comando que executa a aplicação quando o container sobe
ENTRYPOINT ["java", "-jar", "/app/app.jar"]