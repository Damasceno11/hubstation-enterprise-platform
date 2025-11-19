# Usa a mesma base da API principal
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Cria o usuário
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8081

# Comando de execução
ENTRYPOINT ["java", "-jar", "/app/app.jar"]