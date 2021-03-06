/**
 * 
 */
package com.eos.security.impl.service.user;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.util.ReflectionUtil;

/**
 * User tenant DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSUserTenantDAO {

	public static final Label label = DynamicLabel.label("UserTenant");

	private static final String QUERY_CREATE = "MERGE (n:UserTenant {login: {login}, nickName: {nickName}, email: {email}, "
			+ "state: {state}, userTenantId: {userTenantId}}) ON CREATE SET n.created = timestamp(), n.lastUpdate = timestamp() RETURN n";

	private static final String QUERY_CREATE_USER_TENANT_RELATION = "MATCH (userTenant:UserTenant{userTenantId:{userTenantId}}), "
			+ "(tenant:Tenant{alias:{tenantAlias}}) MERGE (tenant)-[r:CONNECTED_USER]->(userTenant) ";

	private static final String QUERY_FIND = "MATCH (tenant:Tenant{alias: {tenantAlias}})"
			+ "-[:CONNECTED_USER]->(userTenant:UserTenant{login: {login}}) RETURN userTenant ";

	@Autowired
	TransactionManager transactionManager;

	public EOSUser createUser(EOSUser user) {
		Map<String, Object> params = userAsMap(user);
		String userTenantId = userTenantId(user.getLogin(), user.getTenantAlias());

		try (ResourceIterator<Node> result = transactionManager.executionEngine().execute(QUERY_CREATE, params)
				.columnAs("n")) {
			if (result.hasNext()) {
				params.clear();
				params.put("userTenantId", userTenantId);
				params.put("tenantAlias", user.getTenantAlias());
				// Create relation
				transactionManager.executionEngine().execute(QUERY_CREATE_USER_TENANT_RELATION, params);
				return user;
			} else {
				return null;
			}
		}
	}

	public EOSUser findByLogin(String login, String tenantAlias) {

		Map<String, Object> params = new HashMap<>(2);
		params.put("tenantAlias", tenantAlias);
		params.put("login", login);

		try (ResourceIterator<Node> result = transactionManager.executionEngine().execute(QUERY_FIND, params)
				.columnAs("userTenant")) {
			if (result.hasNext()) {
				return ReflectionUtil.convert(result.next(), EOSUser.class);
			} else {
				return null;
			}
		}
	}

	private Map<String, Object> userAsMap(EOSUser user) {
		Map<String, Object> params = new HashMap<>(5);
		params.put("login", user.getLogin());
		params.put("nickName", user.getNickName());
		params.put("email", user.getEmail());
		params.put("state", user.getState().name());
		params.put("userTenantId", userTenantId(user.getLogin(), user.getTenantAlias()));

		return params;
	}

	private String userTenantId(String login, String tenantAlias) {
		return login + "." + tenantAlias;
	}

	// public List<EOSUserTenantEntity> findByLogins(List<String> logins, Long tenantId) {
	// return em.createNamedQuery(EOSUserTenantEntity.QUERY_FIND_MULTIPLE, EOSUserTenantEntity.class)
	// .setParameter(EOSUserTenantEntity.PARAM_LOGIN, logins)
	// .setParameter(EOSUserTenantEntity.PARAM_TENANT, tenantId).getResultList();
	// }
	//
	// public List<EOSUserTenantEntity> list(List<EOSState> states, Long tenantId, int limit, int offset) {
	// return em.createNamedQuery(EOSUserTenantEntity.QUERY_LIST, EOSUserTenantEntity.class)
	// .setParameter(EOSUserTenantEntity.PARAM_TENANT, tenantId)
	// .setParameter(EOSUserTenantEntity.PARAM_STATE, states).setFirstResult(offset).setMaxResults(limit)
	// .getResultList();
	// }
	//
	// public EOSUserTenantEntity findByEMail(String email, Long tenantId) {
	//
	// try {
	// return em.createNamedQuery(EOSUserTenantEntity.QUERY_FIND_BY_EMAIL, EOSUserTenantEntity.class)
	// .setParameter(EOSUserTenantEntity.PARAM_EMAIL, email)
	// .setParameter(EOSUserTenantEntity.PARAM_TENANT, tenantId).getSingleResult();
	// } catch (NoResultException e) {
	// return null;
	// }
	// }
	//
	// public void updateState(String login, Long tenantId, EOSState state) {
	// em.createNamedQuery(EOSUserTenantEntity.QUERY_UPDATE_STATE)
	// .setParameter(EOSUserTenantEntity.PARAM_LOGIN, login)
	// .setParameter(EOSUserTenantEntity.PARAM_TENANT, tenantId)
	// .setParameter(EOSUserTenantEntity.PARAM_STATE, state).executeUpdate();
	// }
	//
	// public void deleteUser(String login, Long tenantId) {
	// em.createNamedQuery(EOSUserTenantEntity.QUERY_DELETE_USER)
	// .setParameter(EOSUserTenantEntity.PARAM_TENANT, tenantId)
	// .setParameter(EOSUserTenantEntity.PARAM_LOGIN, login).executeUpdate();
	// }
	//
	// public Long countUsers(String login) {
	// return em.createNamedQuery(EOSUserTenantEntity.QUERY_COUNT_BY_LOGIN, Long.class)
	// .setParameter(EOSUserTenantEntity.PARAM_LOGIN, login).getSingleResult();
	// }
}
