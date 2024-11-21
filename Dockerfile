# Usa a imagem base Amazon Corretto com Java 21
FROM amazoncorretto:21

# Define o diretório de trabalho dentro do container
WORKDIR /api

# Expor a porta 8080 para comunicação
EXPOSE 8080

# Copiar o projeto inteiro para o container
COPY . .

RUN export $(cat .env | xargs)

# Comando para rodar o JAR com opções ajustadas
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar api-erp.jar"]
