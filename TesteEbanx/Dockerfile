# Usa uma imagem leve do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado pelo Maven para dentro do container
# O nome do arquivo pode variar, o * pega qualquer .jar na pasta target
COPY target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]