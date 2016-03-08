package it.soprasteria.pianificazione.v2.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;

@Component
public class FormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return RecordV2Bean.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "badgeNumber", "NotEmpty.badgeNumber");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "idProject", "NotEmpty.idProject");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customer", "customer");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "activityType", "NotEmpty.activityType");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currency", "NotEmpty.currency");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.price");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cons0", "NotEmpty.cons0");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "prod0", "NotEmpty.prod0");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cons1", "NotEmpty.cons1");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "prod1", "NotEmpty.prod1");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cons2", "NotEmpty.cons2");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "prod2", "NotEmpty.prod2");
	}

}
