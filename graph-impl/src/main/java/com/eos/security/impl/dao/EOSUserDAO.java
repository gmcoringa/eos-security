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
import org.springframework.stereotype.Repository;

import com.eos.security.api.vo.EOSUser;
import com.eos.security.impl.service.internal.TransactionManagerImpl;

/**
 * User DAO.
 * 
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSUserDAO {

	public static final Label label = DynamicLabel.label("User");

	private static final String QUERY_FIND = "MATCH (user:User{login : {login}}) RETURN user ";
	
	public EOSUser findUser(String login){
		Map<String, Object> params = new HashMap<>(1);
		params.put("login", login);

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(QUERY_FIND, params).columnAs("user")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}
	
//	@PersistenceContext
//	private EntityManager em;
//
//	/**
//	 * Default constructor.
//	 */
//	public EOSUserDAO() {
//		super(EOSUserEntity.class);
//	}
//
//	/**
//	 * @see com.eos.commons.jpa.AbstractDAO#getEntityManager()
//	 */
//	@Override
//	public EntityManager getEntityManager() {
//		return em;
//	}
//
//	public EOSUserEntity findByEMail(String email) {
//		try {
//			return em.createNamedQuery(EOSUserEntity.QUERY_FIND_BY_EMAIL, EOSUserEntity.class)
//					.setParameter(EOSUserEntity.PARAM_EMAIL, email).getSingleResult();
//		} catch (NoResultException e) {
//			return null;
//		}
//	}
//
//	public void deleteUser(String login) {
//		em.createNamedQuery(EOSUserEntity.QUERY_DELETE).setParameter(EOSUserEntity.PARAM_LOGIN, login).executeUpdate();
//	}
	
	private EOSUser convertNode(Node node){
		EOSUser user = new EOSUser();
		user.setLogin((String) node.getProperty("login")).setEmail((String) node.getProperty("email"));
		
		return user;
	}
}
