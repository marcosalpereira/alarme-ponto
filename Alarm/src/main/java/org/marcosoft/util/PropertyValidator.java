package org.marcosoft.util;

public interface PropertyValidator {
	void validate(String property, String value) throws ValidatorException;
}