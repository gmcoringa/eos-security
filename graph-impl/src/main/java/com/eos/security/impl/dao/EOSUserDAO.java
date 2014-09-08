/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eos.common.util.StringUtil;
import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.util.ReflectionUtil;

/**
 * User DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSUserDAO {

	public static final Label label = DynamicLabel.label("User");

	private static final String QUERY_CREATE = "MERGE (n:User {login: {login}, firstName: {firstName}, lastName: {lastName}, "
			+ "personalMail: {personalMail}, type: {type}}) ON CREATE SET n.created = timestamp(), n.lastUpdate = timestamp() RETURN n";
	private static final String QUERY_FIND = "MATCH (user:User{login : {login}}) RETURN user ";

	@Autowired
	TransactionManager transactionManager;

	public EOSUser findUser(String login) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("login", login);

		try (ResourceIterator<Node> result = transactionManager.executionEngine()
				.execute(QUERY_FIND, params).columnAs("user")) {
			if (result.hasNext()) {
				return ReflectionUtil.convert(result.next(), EOSUser.class);
			} else {
				return null;
			}
		}
	}

	public EOSUser createUser(EOSUser user, String password) {
		try (ResourceIterator<Node> result = transactionManager.executionEngine()
				.execute(QUERY_CREATE, userAsMap(user, password)).columnAs("n")) {
			if (result.hasNext()) {
				return ReflectionUtil.convert(result.next(), EOSUser.class);
			} else {
				return null;
			}
		}
	}

	private Map<String, Object> userAsMap(EOSUser user, String password) {
		Map<String, Object> params = new HashMap<>(6);
		params.put("login", user.getLogin());
		params.put("firstName", user.getFirstName());
		params.put("lastName", user.getLastName());
		params.put("personalMail", user.getPersonalMail());
		params.put("type", user.getType().name());

		if (!StringUtil.isEmpty(password)) {
			params.put("password", password);
		}

		return params;
	}
	// public EOSUserEntity findByEMail(String email) {
	// try {
	// return em.createNamedQuery(EOSUserEntity.QUERY_FIND_BY_EMAIL, EOSUserEntity.class)
	// .setParameter(EOSUserEntity.PARAM_EMAIL, email).getSingleResult();
	// } catch (NoResultException e) {
	// return null;
	// }
	// }
	//
	// public void deleteUser(String login) {
	// em.createNamedQuery(EOSUserEntity.QUERY_DELETE).setParameter(EOSUserEntity.PARAM_LOGIN, login).executeUpdate();
	// }
}
