package com.orrin.sca.component.utils.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author orrin.zhang on 2017/8/17.
 * 参数校验
 */
public class ParameterValidatorUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterValidatorUtils.class);

	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	private static final Validator validator = factory.getValidator();

	public static<T> String checkUserBusinessParameter(T t){

		StringBuilder messages = new StringBuilder();
		boolean parameterRightFlag = true;

		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		for(ConstraintViolation<T> constraintViolation : constraintViolations) {
			LOGGER.info(constraintViolation.getPropertyPath() + ": ");
			LOGGER.info(constraintViolation.getMessage());
			if(parameterRightFlag){
				parameterRightFlag = false;
			}
			messages.append(constraintViolation.getMessage()).append(",");
		}

		if(!parameterRightFlag){
			return messages.substring(messages.length()-1);
		}else {
			return "";
		}
	}
}