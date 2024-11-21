# Usa a imagem base Amazon Corretto com Java 21
FROM amazoncorretto:21

# Instalar o Maven de forma eficiente
RUN yum install -y maven && yum clean all

# Define o diretório de trabalho dentro do container
WORKDIR /api

# Expor a porta 8080 para comunicação
EXPOSE 8080

# Copiar o projeto inteiro para o container
COPY . .

# Adiciona o arquivo JAR ao container
COPY --from=build ./target/maisPraTi-0.0.1-SNAPSHOT.jar api-erp.jar
RUN ls -al /build

# Copiar o script de testes e garantir permissão para execução
COPY ./run-tests.sh ./run-tests.sh
RUN chmod +x ./run-tests.sh

# Definir memória máxima para a JVM e melhorar a eficiência
# ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Comando para rodar o JAR com opções ajustadas
EENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar api-erp.jar"]
