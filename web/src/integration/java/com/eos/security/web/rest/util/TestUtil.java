package com.eos.security.web.rest.util;

import com.eos.common.EOSUserType;
import com.eos.security.api.vo.EOSUser;

public class TestUtil {

	public static EOSUser buildUser(String label) {
		return new EOSUser().setEmail(label + "@tenanttest.com").setFirstName("First " + label)
				.setLastName("Last " + label).setLogin("login_" + label).setNickName("Nick " + label)
				.setPersonalMail("personal@testtenant.com").setType(EOSUserType.USER);
	}
}
