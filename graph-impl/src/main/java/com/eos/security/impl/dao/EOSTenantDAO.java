/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.springframework.stereotype.Repository;

import com.eos.common.EOSState;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.impl.service.internal.TransactionManagerImpl;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSTenantDAO {
	public static final Label label = DynamicLabel.label("Tenant");

	public EOSTenant create(EOSTenant tenant) {

		String queryString = "MERGE (n:Tenant {alias: {alias}, name: {name}, description: {description}, "
				+ "state: {state}}) RETURN n";
		Map<String, Object> params = new HashMap<>(4);
		params.put("alias", tenant.getAlias());
		params.put("name", tenant.getName());
		params.put("description", tenant.getName());
		params.put("state", tenant.getState().name());

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(queryString, params).columnAs("n")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}

	public EOSTenant find(String alias) {

		String queryString = "MATCH (tenant:Tenant{alias : {alias}}) RETURN tenant ";
		Map<String, Object> params = new HashMap<>(1);
		params.put("alias", alias);

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(queryString, params).columnAs("tenant")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}

	public Set<EOSTenant> listTenants(Set<EOSState> states, int limit, int offset) {
		String queryString = null;
		Map<String, Object> params = new HashMap<>(1);
		Set<EOSTenant> tenants = new HashSet<>(limit);

		if (states == null || states.isEmpty()) {
			queryString = "MATCH (tenant:Tenant) RETURN tenant ";
		} else {
			queryString = "MATCH (tenant:Tenant) WHERE tenant.state IN {states} RETURN tenant ORDER BY tenant.name ";
			params.put("states", states);
		}

		queryString += "SKIP {skip} LIMIT {limit} ";
		params.put("limit", limit);
		params.put("skip", offset);

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(queryString, params).columnAs("tenant")) {
			while (result.hasNext()) {
				tenants.add(convertNode(result.next()));
			}
		}

		return tenants;
	}

	public void purgeTenant(String alias) {
		// em.createNamedQuery(EOSTenantEntity.QUERY_PURGE).setParameter("id", id).executeUpdate();
	}

	public Set<EOSTenant> findTenants(Set<String> aliases) {
		String queryString = "MATCH (tenant:Tenant) WHERE tenant.alias IN {aliases} RETURN tenant ";
		Map<String, Object> params = new HashMap<>(1);
		params.put("aliases", aliases);
		Set<EOSTenant> tenants = new HashSet<>(aliases.size());

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(queryString, params).columnAs("tenant")) {
			while (result.hasNext()) {
				tenants.add(convertNode(result.next()));
			}
		}
		return tenants;
	}

	private EOSTenant convertNode(Node node) {
		EOSTenant tenant = new EOSTenant();
		tenant.setAlias((String) node.getProperty("alias")).setName((String) node.getProperty("name"))
				.setDescription((String) node.getProperty("description"))
				.setState(EOSState.valueOf((String) node.getProperty("state")));
		return tenant;
	}
}
