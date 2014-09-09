package com.eos.security.impl.tenant;

import static com.eos.security.impl.test.util.EOSTestUtil.buildTenant;

import com.eos.common.EOSState;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.impl.service.tenant.EOSTenantDAO;

public class EOSTenantDAOUtil {


	public static EOSTenant create(String alias, EOSTenantDAO tenantDAO) {
		return create(alias, EOSState.DISABLED, tenantDAO);
	}

	public static EOSTenant create(String alias, EOSState state, EOSTenantDAO tenantDAO) {
		return tenantDAO.create(buildTenant(alias).setState(state));
	}

}
