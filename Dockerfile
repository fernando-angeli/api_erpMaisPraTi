# Usa a imagem base Amazon Corretto com Java 21
FROM amazoncorretto:21

# Define o diretório de trabalho
WORKDIR /api

# Copia o JAR e o script para o container
COPY target/maisPraTi-0.0.1-SNAPSHOT.jar maisPraTi-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh entrypoint.sh

# Permissão para execução do script
RUN chmod +x entrypoint.sh

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["./entrypoint.sh"]
