package com.erp.maisPraTi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentação da API - ERP+praTi")
                        .version("1.0.0")
                        .description("Esta API é utilizada para gerenciar um sistema ERP, oferecendo funcionalidades como: \n\n"
                                + "- **Gerenciamento de Clientes**: Criar, editar, remover e consultar informações de clientes.\n"
                                + "- **Gerenciamento de Produtos**: Cadastrar novos produtos, atualizar informações, e controlar estoque.\n"
                                + "- **Gestão de Pedidos de Compra e Venda**: Criar pedidos de compra e venda, verificar status e processar ordens.\n"
                                + "- **Controle de Estoque**: Monitorar entradas e saídas de produtos e gerenciar inventário.\n"
                                + "- **Contas a Pagar e Receber**: Gerenciamento financeiro, incluindo faturamento e controle de despesas.\n"
                                + "\nA API também oferece suporte a autenticação e autorização com JWT, garantindo segurança e controle de acesso aos recursos do sistema."));
    }
}
