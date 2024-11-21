package com.erp.maisPraTi.service.validations;

import com.erp.maisPraTi.dto.partyDto.PartyDto;
import com.erp.maisPraTi.service.exceptions.InvalidDocumentException;
import com.erp.maisPraTi.util.CNPJValidator;
import com.erp.maisPraTi.util.CPFValidator;
import com.erp.maisPraTi.util.StateRegistrationValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.erp.maisPraTi.enums.TypePfOrPj.PF;
import static com.erp.maisPraTi.enums.TypePfOrPj.PJ;

public class DocumentsValidator implements ConstraintValidator<DocumentsValid, PartyDto>{

    @Override
    public void initialize(DocumentsValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PartyDto partyDto, ConstraintValidatorContext constraintValidatorContext) {

        if(partyDto.getTypePfOrPj().equals(PF) && !CPFValidator.validateCpf(partyDto.getCpfCnpj()))
            throw new InvalidDocumentException("CPF inválido.");

        if (partyDto.getTypePfOrPj().equals(PJ)) {
            if (!CNPJValidator.validateCnpj(partyDto.getCpfCnpj()))
                throw new InvalidDocumentException("CNPJ inválido.");
            if (!StateRegistrationValidator.validateStateRegistration(partyDto.getStateRegistration()))
                throw new InvalidDocumentException("Obrigatório informar uma Inscrição Estadual ou isento para tipo PJ.");
        }
        return true;
    }
}
