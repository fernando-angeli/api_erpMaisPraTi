package com.erp.maisPraTi.util;

import java.util.regex.Pattern;

public class StateRegistrationValidator {

    // Padrão Regex: "isento" ou números com separadores válidos (/, ., -)
    private static final Pattern STATE_REGISTRATION_PATTERN =
            Pattern.compile("^(isento|[\\d./-]+)$", Pattern.CASE_INSENSITIVE);

    public static boolean validateStateRegistration(String stateRegistration) {
        return stateRegistration != null && STATE_REGISTRATION_PATTERN.matcher(stateRegistration).matches();
    }
}
