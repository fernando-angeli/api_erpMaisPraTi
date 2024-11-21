
# API ERP Mais Pra Ti

Este repositório contém a API de um sistema de ERP (Enterprise Resource Planning) desenvolvido em Java com Spring Boot. A API oferece funcionalidades para gerenciamento de clientes, produtos, pedidos de compra, vendas e controle de estoque, com autenticação baseada em JWT e OAuth2.

## Funcionalidades

A API fornece as seguintes funcionalidades:

- **Gestão de Clientes**: Cadastro, atualização, e exclusão de clientes.
- **Gestão de Produtos**: Cadastro, atualização e exclusão de produtos no sistema.
- **Pedidos de Compra**: Criação e gerenciamento de pedidos de compra para reabastecimento de estoque.
- **Vendas e Controle de Estoque**: Registro de vendas e controle de saída de produtos.
- **Autenticação e Autorização**: Implementação de segurança com OAuth2 e JWT para autenticação de usuários.
- **Gestão de Estoque**: Controle de estoque, com reserva de itens vendidos e controle de chegada de produtos comprados.

## Tecnologias Utilizadas

- **Java 21**: Linguagem de programação principal.
- **Spring Boot 3.3.4**: Framework para construção da API.
- **Spring Security & OAuth2**: Para segurança, com autenticação baseada em JWT.
- **MySQL**: Banco de dados para persistência de dados.
- **JPA & Hibernate**: Para mapeamento de objetos Java para o banco de dados.
- **Docker**: Para containerização da aplicação.

## Como Executar

### Pré-requisitos

- Java 21 ou superior
- Maven 3.x ou superior
- MySQL instalado e rodando
- Postman ou ferramenta similar para testar a API

### Passos para Executar

1. Clone este repositório:

   ```bash
   git clone https://github.com/fernando-angeli/api_erpMaisPraTi.git
   cd api_erpMaisPraTi
   ```

2. Configure o banco de dados MySQL no arquivo `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/erp_maisprati
   spring.datasource.username=root
   spring.datasource.password=senha
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Compile o projeto com Maven:

   ```bash
   mvn clean install
   ```

4. Execute a aplicação:

   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.

## Endpoints da API

### Autenticação

- **POST /auth/login**: Realiza o login do usuário e retorna o token JWT.

### Clientes

- **GET /clientes**: Lista todos os clientes.
- **GET /clientes/{id}**: Retorna um cliente específico.
- **POST /clientes**: Cria um novo cliente.
- **PUT /clientes/{id}**: Atualiza um cliente existente.
- **DELETE /clientes/{id}**: Exclui um cliente.

### Produtos

- **GET /produtos**: Lista todos os produtos.
- **GET /produtos/{id}**: Retorna um produto específico.
- **POST /produtos**: Cria um novo produto.
- **PUT /produtos/{id}**: Atualiza um produto existente.
- **DELETE /produtos/{id}**: Exclui um produto.

### Pedidos de Compra

- **GET /pedidos-compra**: Lista todos os pedidos de compra.
- **POST /pedidos-compra**: Cria um novo pedido de compra.

### Vendas

- **POST /vendas**: Registra uma venda e atualiza o estoque.

### Estoque

- **GET /estoque**: Consulta o estoque atual de produtos.
- **PUT /estoque/{id}**: Atualiza a quantidade de um produto no estoque.

## Contribuições

1. Faça um fork deste repositório.
2. Crie uma branch para a sua feature (`git checkout -b feature/novafeature`).
3. Faça commit das suas alterações (`git commit -am 'Adicionando nova feature'`).
4. Faça um push para a branch (`git push origin feature/novafeature`).
5. Abra um Pull Request.

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais informações.
