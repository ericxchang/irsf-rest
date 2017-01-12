package com.iconectiv.irsf.json.vaidation;

import com.iconectiv.irsf.json.annotation.JsonRule;
import com.iconectiv.irsf.json.annotation.JsonValidate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonValidator {
	private static Logger log = LoggerFactory.getLogger(JsonValidator.class);


	private List<String> report = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public <T> T validate(Object obj) throws JsonValidationException {
		Class<?> classOfObject = obj.getClass();

		if (!classOfObject.isAnnotationPresent(JsonValidate.class)) {
			return (T) obj;
		}

		for (Field field : classOfObject.getDeclaredFields()) {
			validateField(field, obj);
		}

		if (!this.report.isEmpty()) {
			throw new JsonValidationException(StringUtils.join(this.report, ";"));
		}

		return (T) obj;
	}

	private void validateField(Field field, Object obj) {
		if (!field.isAnnotationPresent(JsonRule.class)) {
			return;
		}

		JsonRule rule = field.getAnnotation(JsonRule.class);
		try {
			if (rule.required() && FieldUtils.readField(field, obj, true) == null) {
				this.report.add("Field " + field.getName() + " can not be null");
			}

			//TODO check children recursively
			
		} catch (IllegalAccessException e) {
			log.error("Error to access field: ", e);
			this.report.add("Error to access field " + field.getName() + ": " + e.getMessage());
		}

	}

}
