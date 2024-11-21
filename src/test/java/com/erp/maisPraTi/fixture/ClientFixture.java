package com.erp.maisPraTi.fixture;

import com.erp.maisPraTi.enums.PartyStatus;
import com.erp.maisPraTi.enums.TypePfOrPj;
import com.erp.maisPraTi.model.Client;

import java.math.BigDecimal;

public class ClientFixture {

    public static Client clientFixture(){
        Client client = new Client();
        client.setFullName("José da Silva");
        client.setAddress("Rua de Testes");
        client.setNumber("220");
        client.setDistrict("Santo Afonso");
        client.setCity("Porto Alegre");
        client.setState("RS");
        client.setCountry("Brasil");
        client.setZipCode("95.630-000");
        client.setEmail("jose@gmail.com");
        client.setPhoneNumber("51984845858");
        client.setCpfCnpj("639.587.770-40");
        client.setTypePfOrPj(TypePfOrPj.PF);
        client.setCreditLimit(new BigDecimal(5000.00));
        client.setStatus(PartyStatus.ACTIVE);
        return client;
    }
}
