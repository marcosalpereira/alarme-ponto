package org.marcosoft.alarm;

import org.marcosoft.util.PropertyValidator;
import org.marcosoft.util.ValidatorException;

/**
 * Valida as opcoes.
 */
public class Validator implements PropertyValidator {

	@Override
	public void validate(String property, String value)
			throws ValidatorException {
		
		if (property.equals(Alarm.PROPERTY_BEEPER_TIMES)) {
			validateInteger(value);
			
		} else if (property.equals(Alarm.PROPERTY_BEEPER_COMMAND)) {
			try {
				Runtime.getRuntime().exec(value);
			} catch (Exception e) {
				throw new ValidatorException("Comando Inválido!\n" + e.getMessage());
			}
			
		} else if (property.equals(Alarm.PROPERTY_BEEPER_PAUSE)) {
			validateInteger(value);			
		}
	}

	private void validateInteger(String value) throws ValidatorException {
		try {
			int parseInt = Integer.parseInt(value);
			if (parseInt <= 0) {
				throw new ValidatorException("Valor inválido!");
			}
		} catch (NumberFormatException e) {
			throw new ValidatorException("Valor inválido!");
		}
	}

}
