# Etapa 1: Build da aplicação
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Executar a aplicação
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Otimização de memória para o Free Tier (512MB RAM)
ENTRYPOINT ["java", "-Xmx300m", "-jar", "app.jar"]