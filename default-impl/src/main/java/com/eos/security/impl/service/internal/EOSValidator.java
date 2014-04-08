/**
 * 
 */
package com.eos.security.impl.service.internal;

import java.util.Collection;

import com.eos.common.exception.EOSValidationException;
import com.eos.commons.jpa.EntityFieldSizes;
import com.eos.security.api.vo.EOSGroup;
import com.eos.security.api.vo.EOSRole;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.api.vo.EOSUser;

/**
 * Validation utility.
 * 
 * @author santos.fabiano
 * 
 */
public final class EOSValidator {

	private static void checkErrors(EOSErrorFactory factory) throws EOSValidationException {
		if (factory.hasErrors()) {
			throw new EOSValidationException("Validation errors", factory.getErrors());
		}
	}

	public static void validateGroup(EOSGroup group) throws EOSValidationException {
		EOSErrorFactory factory = new EOSErrorFactory();

		factory.addError(ValidationUtils.validateString("group name", group.getName(), true, EntityFieldSizes.MINIMUM,
				EntityFieldSizes.DATA_TINY));
		factory.addError(ValidationUtils.maxLength("group description", group.getDescription(),
				EntityFieldSizes.DATA_STANDART));
		factory.addError(ValidationUtils.requiredNotNull("group level", group.getLevel()));

		checkErrors(factory);
	}

	public static void validateUser(EOSUser user) throws EOSValidationException {
		EOSErrorFactory factory = new EOSErrorFactory();
		;
		factory.addError(ValidationUtils.validatePathString("user login", user.getLogin(), EntityFieldSizes.MINIMUM,
				EntityFieldSizes.DATA_TINY, true));
		factory.addError(ValidationUtils.validateEmail("personal email", user.getPersonalMail(), true));
		factory.addError(ValidationUtils.validateString("user first name", user.getFirstName(), true,
				EntityFieldSizes.MINIMUM, EntityFieldSizes.DATA_SMALL));
		factory.addError(ValidationUtils.validateString("user last name", user.getLastName(), true,
				EntityFieldSizes.MINIMUM, EntityFieldSizes.DATA_SMALL));
		factory.addError(ValidationUtils.validateString("user nickname", user.getNickName(), false,
				EntityFieldSizes.MINIMUM, EntityFieldSizes.DATA_TINY));
		factory.addError(ValidationUtils.validateEmail("user tenant email", user.getEmail(), false));

		checkErrors(factory);
	}

	public static void validateRole(EOSRole role) throws EOSValidationException {
		EOSErrorFactory factory = new EOSErrorFactory();

		factory.addError(ValidationUtils.validatePathString("role code", role.getCode(), EntityFieldSizes.MINIMUM,
				EntityFieldSizes.DATA_TINY, true));
		factory.addError(ValidationUtils.maxLength("role description", role.getDescription(),
				EntityFieldSizes.DATA_STANDART));
		factory.addError(ValidationUtils.requiredNotNull("role level", role.getLevel()));

		checkErrors(factory);
	}

	public static void validateTenant(EOSTenant tenant) throws EOSValidationException {
		EOSErrorFactory factory = new EOSErrorFactory();

		factory.addError(ValidationUtils.validateString("tenant name", tenant.getName(), true,
				EntityFieldSizes.MINIMUM, EntityFieldSizes.DATA_SMALL));
		factory.addError(ValidationUtils.maxLength("tenant description", tenant.getDescription(),
				EntityFieldSizes.DATA_LARGE));

		checkErrors(factory);
	}

	public static void validatePermissions(Collection<String> permissions) throws EOSValidationException {
		EOSErrorFactory factory = new EOSErrorFactory();

		for (String permission : permissions) {
			factory.addError(ValidationUtils.validatePathString("role permission", permission,
					EntityFieldSizes.MINIMUM, EntityFieldSizes.DATA_TINY, true));
		}

		checkErrors(factory);
	}

	public static void validatePassword(String password) throws EOSValidationException {
		EOSErrorFactory factory = new EOSErrorFactory();

		factory.addError(ValidationUtils.validateString("user password", password, true,
				EntityFieldSizes.PASSWORD_MIN_SIZE, EntityFieldSizes.DATA_TINY));
		// TODO validate password strength, see:
		// https://code.google.com/p/vt-middleware/wiki/vtpassword

		checkErrors(factory);
	}
}
