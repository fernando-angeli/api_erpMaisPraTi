package com.erp.maisPraTi.util;

public class CNPJValidator {

    public static boolean validateCnpj(String cnpj) {

        // Teste caracteres especiais (diferentes de numericos)
        if(cnpj.matches(".*[^0-9./-].*"))
            return false;

        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se o CNPJ tem 14 dígitos
        if (cnpj.length() != 14)
            return false;

        try {
            // Calcula o primeiro dígito verificador
            int soma = 0, peso = 5;
            for (int i = 0; i < 12; i++) {
                soma += Integer.parseInt(cnpj.substring(i, i + 1)) * peso;
                peso--;
                if (peso == 1)
                    peso = 9;
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 > 9)
                digito1 = 0;

            // Calcula o segundo dígito verificador
            soma = 0;
            peso = 6;
            for (int i = 0; i < 13; i++) {
                soma += Integer.parseInt(cnpj.substring(i, i + 1)) * peso;
                peso--;
                if (peso == 1)
                    peso = 9;
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 > 9)
                digito2 = 0;

            // Verifica se os dígitos verificadores calculados são iguais aos informados
            return Integer.parseInt(cnpj.substring(12, 13)) == digito1 && Integer.parseInt(cnpj.substring(13)) == digito2;
        } catch (NumberFormatException e) {
            return false; // Em caso de erro ao converter para int
        }
    }

}
