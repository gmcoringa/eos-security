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
import com.eos.common.util.StringUtil;
import com.eos.security.api.vo.EOSTenant;
import com.eos.security.impl.service.internal.TransactionManagerImpl;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSTenantDAO {
	public static final Label label = DynamicLabel.label("Tenant");

	private static final String QUERY_CREATE = "MERGE (n:Tenant {alias: {alias}, name: {name}, description: {description}, "
			+ "state: {state}}) RETURN n";
	private static final String QUERY_FIND = "MATCH (tenant:Tenant{alias : {alias}}) RETURN tenant ";
	private static final String QUERY_UPDATE = "MATCH (tenant:Tenant{alias : {alias}}) SET tenant.name = {name}, "
			+ "tenant.description = {description} RETURN tenant ";
	private static final String QUERY_UPDATE_STATE = "MATCH (tenant:Tenant{alias : {alias}}) SET tenant.state = {state} RETURN tenant ";
	private static final String QUERY_FIND_BY_ALIASES = "MATCH (tenant:Tenant) WHERE tenant.alias IN {aliases} RETURN tenant ";
	private static final String QUERY_LIST = "MATCH (tenant:Tenant) RETURN tenant SKIP {skip} LIMIT {limit} ";
	private static final String QUERY_LIST_BY_STATE = "MATCH (tenant:Tenant) WHERE tenant.state IN {states} RETURN tenant "
			+ "ORDER BY tenant.name SKIP {skip} LIMIT {limit} ";
	private static final String QUERY_PURGE = "MATCH (tenant { alias: {alias} })-[r]-() DELETE tenant, r";

	public EOSTenant create(EOSTenant tenant) {

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(QUERY_CREATE, tenantAsMap(tenant)).columnAs("n")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}

	public EOSTenant find(String alias) {

		Map<String, Object> params = new HashMap<>(1);
		params.put("alias", alias);

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(QUERY_FIND, params).columnAs("tenant")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}

	public EOSTenant update(EOSTenant tenant) {
		Map<String, Object> params = new HashMap<>(3);
		params.put("alias", tenant.getAlias());
		params.put("name", tenant.getName());
		params.put("description", tenant.getDescription());

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(QUERY_UPDATE, params).columnAs("tenant")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}

	public EOSTenant update(String alias, EOSState state) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("alias", alias);
		params.put("state", state.name());

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(QUERY_UPDATE_STATE, params).columnAs("tenant")) {
			if (result.hasNext()) {
				return convertNode(result.next());
			} else {
				return null;
			}
		}
	}

	public Set<EOSTenant> listTenants(Set<EOSState> states, int limit, int offset) {
		String query = null;
		Map<String, Object> params = new HashMap<>(3);
		Set<EOSTenant> tenants = new HashSet<>(limit);

		if (states == null || states.isEmpty()) {
			query = QUERY_LIST;
		} else {
			query = QUERY_LIST_BY_STATE;
			params.put("states", states);
		}

		params.put("limit", limit);
		params.put("skip", offset);

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(query, params).columnAs("tenant")) {
			while (result.hasNext()) {
				tenants.add(convertNode(result.next()));
			}
		}

		return tenants;
	}

	public void purgeTenant(String alias) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("alias", alias);
		TransactionManagerImpl.transactionManager().executionEngine().execute(QUERY_PURGE, params);
	}

	public Set<EOSTenant> findTenants(Set<String> aliases) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("aliases", aliases);
		Set<EOSTenant> tenants = new HashSet<>(aliases.size());

		try (ResourceIterator<Node> result = TransactionManagerImpl.transactionManager().executionEngine()
				.execute(QUERY_FIND_BY_ALIASES, params).columnAs("tenant")) {
			while (result.hasNext()) {
				tenants.add(convertNode(result.next()));
			}
		}

		return tenants;
	}

	// ################################
	// # Utilities
	// ###############################

	private EOSTenant convertNode(Node node) {
		EOSTenant tenant = new EOSTenant();
		tenant.setAlias((String) node.getProperty("alias")).setName((String) node.getProperty("name"))
				.setDescription((String) node.getProperty("description"))
				.setState(EOSState.valueOf((String) node.getProperty("state")));
		return tenant;
	}

	private Map<String, Object> tenantAsMap(EOSTenant tenant) {
		Map<String, Object> props = new HashMap<>(4);

		if (!StringUtil.isEmpty(tenant.getAlias())) {
			props.put("alias", tenant.getAlias());
		}

		if (!StringUtil.isEmpty(tenant.getName())) {
			props.put("name", tenant.getName());
		}

		if (!StringUtil.isEmpty(tenant.getDescription())) {
			props.put("description", tenant.getDescription());
		}

		if (tenant.getState() != null) {
			props.put("state", tenant.getState().name());
		}

		return props;
	}
}
