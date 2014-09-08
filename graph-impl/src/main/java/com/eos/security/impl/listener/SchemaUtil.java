/**
 * 
 */
package com.eos.security.impl.listener;

import java.util.Set;

import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eos.common.util.CollectionUtil;
import com.eos.security.impl.dao.EOSTenantDAO;
import com.eos.security.impl.dao.EOSTenantDataDAO;
import com.eos.security.impl.dao.EOSUserDAO;
import com.eos.security.impl.dao.EOSUserTenantDAO;
import com.eos.security.impl.service.TransactionManager;

/**
 * @author fabiano.santos
 * 
 */
public class SchemaUtil {

	private static final Logger log = LoggerFactory.getLogger(SchemaUtil.class);

	public static void createSchema(TransactionManager transactionManager) {
		transactionManager.begin();
		try {
			Schema schema = transactionManager.graphDB().schema();
			// Tenant
			createConstraint(schema, EOSTenantDAO.label, CollectionUtil.asSet("alias"));
			createIndex(schema, EOSTenantDAO.label, CollectionUtil.asSet("state"));
			// Tenant Data
			createConstraint(schema, EOSTenantDAO.label, CollectionUtil.asSet("metaId"));
			createIndex(schema, EOSTenantDataDAO.label, CollectionUtil.asSet("key"));
			// User
			createConstraint(schema, EOSUserDAO.label, CollectionUtil.asSet("login"));
			createConstraint(schema, EOSUserTenantDAO.label, CollectionUtil.asSet("login"));
		} finally {
			transactionManager.commit();
		}
	}

	private static void createConstraint(Schema schema, Label label, Set<String> fields) {
		for (String field : fields) {
			try {
				schema.constraintFor(label).assertPropertyIsUnique(field).create();
			} catch (ConstraintViolationException e) {
				log.warn("Constraint already exist [label=" + label.name() + ", field=" + field + "]");
				log.debug("Constraint already exists", e);
			}
		}
	}

	private static void createIndex(Schema schema, Label label, Set<String> fields) {
		for (String field : fields) {
			try {
				schema.indexFor(label).on(field).create();
			} catch (ConstraintViolationException e) {
				log.warn("Index already exist [label=" + label.name() + ", field=" + field + "]");
				log.debug("Index already exists", e);
			}
		}
	}
}
