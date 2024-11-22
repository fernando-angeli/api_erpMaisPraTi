#!/bin/bash
mvn clean test

echo "Iniciando os testes..."

# Rodar os testes com Maven
mvn test

# Capturar o status dos testes
if [ $? -eq 0 ]; then
    echo "Testes concluídos com sucesso!"
    exit 0
else
    echo "Falha nos testes."
    exit 1
fi