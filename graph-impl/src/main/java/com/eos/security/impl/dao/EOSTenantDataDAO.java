/**
 * 
 */
package com.eos.security.impl.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eos.security.impl.service.TransactionManager;

/**
 * @author santos.fabiano
 * 
 */
@Repository
public class EOSTenantDataDAO {

	private static final Logger log = LoggerFactory.getLogger(EOSTenantDataDAO.class);

	public static final Label label = DynamicLabel.label("TenantData");

	private static final String QUERY_CREATE = "MERGE (meta:TenantData {metaId: {metaId}, key: {key}, value: {value}, "
			+ "tenantAlias: {tenantAlias}}) ON CREATE SET meta.created = timestamp(), meta.lastUpdate = timestamp() ";

	private static final String QUERY_CREATE_META_RELATION = "MATCH (meta:TenantData{metaId:{metaId}}), "
			+ "(tenant:Tenant{alias:{tenantAlias}}) MERGE (tenant)-[r:HAS_META]->(meta) ";

	private static final String QUERY_UPDATE = "MATCH (tenant:Tenant{alias: {tenantAlias}})-[:HAS_META]->"
			+ "(meta:TenantData{key: {key}}) SET meta.value = {value}, meta.lastUpdate = timestamp() ";

	private static final String QUERY_PURGE = "MATCH (tenant:Tenant{alias: {tenantAlias}})-[:HAS_META]->(meta:TenantData) "
			+ "WHERE meta.key IN {key} WITH meta MATCH meta-[r]-() DELETE r,meta";
	private static final String QUERY_FIND_BY_KEYS = "MATCH (tenant:Tenant{alias: {tenantAlias}})-[:HAS_META]->(meta:TenantData) "
			+ "WHERE meta.key IN {key} RETURN meta ";
	private static final String QUERY_FIND_BY_KEY = "MATCH (tenant:Tenant{alias: {tenantAlias}})-[:HAS_META]->"
			+ "(meta:TenantData{key: {key}}) RETURN meta ";
	private static final String QUERY_FIND_ALL = "MATCH (tenant:Tenant{alias: {tenantAlias}})-[:HAS_META]->(meta:TenantData) "
			+ "RETURN meta ORDER BY meta.key SKIP {skip} LIMIT {limit} ";

	@Autowired
	TransactionManager transactionManager;

	public void createTenantData(String tenantAlias, String key, String value) {
		Map<String, Object> params = new HashMap<>(4);
		String metaId = metaId(tenantAlias, key);
		params.put("metaId", metaId);
		params.put("key", key);
		params.put("value", value);
		params.put("tenantAlias", tenantAlias);

		// Create node
		transactionManager.executionEngine().execute(QUERY_CREATE, params);

		params.clear();
		params.put("metaId", metaId);
		params.put("tenantAlias", tenantAlias);
		// Create relation
		transactionManager.executionEngine().execute(QUERY_CREATE_META_RELATION, params);
	}

	public void updateTenantData(String tenantAlias, String key, String value) {
		Map<String, Object> params = new HashMap<>(3);
		params.put("tenantAlias", tenantAlias);
		params.put("key", key);
		params.put("value", value);

		ExecutionResult result = transactionManager.executionEngine().execute(QUERY_UPDATE, params);
		log.debug("Tenant[" + tenantAlias + "] updated data: " + result.dumpToString());
	}

	public void deleteTenantData(String tenantAlias, Set<String> keys) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("tenantAlias", tenantAlias);
		params.put("key", keys);
		ExecutionResult result = transactionManager.executionEngine().execute(QUERY_PURGE, params);
		log.debug("Tenant[" + tenantAlias + "] deleted data: " + result.dumpToString());
	}

	public String findTenantDataValue(String tenantAlias, String key) {
		Map<String, Object> params = new HashMap<>(2);

		params.put("tenantAlias", tenantAlias);
		params.put("key", key);

		try (ResourceIterator<Node> result = transactionManager.executionEngine().execute(QUERY_FIND_BY_KEY, params)
				.columnAs("meta")) {
			if (result.hasNext()) {
				return (String) result.next().getProperty("value");
			} else {
				return null;
			}
		}
	}

	public Map<String, String> findTenantDataValues(String tenantAlias, Set<String> keys) {
		Map<String, Object> params = new HashMap<>(2);
		Map<String, String> metas = new HashMap<>(keys.size());

		params.put("tenantAlias", tenantAlias);
		params.put("key", keys);

		try (ResourceIterator<Node> result = transactionManager.executionEngine().execute(QUERY_FIND_BY_KEYS, params)
				.columnAs("meta")) {
			while (result.hasNext()) {
				Node node = result.next();
				metas.put((String) node.getProperty("key"), (String) node.getProperty("value"));
			}
		}

		return metas;
	}

	public Map<String, String> listTenantData(String tenantAlias, int limit, int offset) {
		Map<String, Object> params = new HashMap<>(3);
		Map<String, String> metas = new HashMap<>(limit);

		params.put("tenantAlias", tenantAlias);
		params.put("skip", offset);
		params.put("limit", limit);

		try (ResourceIterator<Node> result = transactionManager.executionEngine().execute(QUERY_FIND_ALL, params)
				.columnAs("meta")) {
			while (result.hasNext()) {
				Node node = result.next();
				metas.put((String) node.getProperty("key"), (String) node.getProperty("value"));
			}
		}

		return metas;
	}

	private String metaId(String tenantAlias, String key) {
		return tenantAlias + ":" + key;
	}
}
