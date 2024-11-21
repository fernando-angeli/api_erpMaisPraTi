FROM amazoncorretto:21

# Define o diretório de trabalho dentro do container
WORKDIR /api-erp

# Expoe a porta 8080
EXPOSE 8080

# Adiciona o arquivo JAR ao container
COPY ./target/maisPraTi-0.0.1-SNAPSHOT.jar api-erp.jar

# Copiar o script de testes para o contêiner
COPY ./run-tests.sh /api-erp/run-tests.sh
# Garante a permissão para execução do script de teste
RUN chmod +x /api-erp/run-tests.sh

# Comando para rodar o JAR correto
ENTRYPOINT ["java", "-jar", "api-erp.jar"]