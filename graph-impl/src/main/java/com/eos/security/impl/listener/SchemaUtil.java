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
import com.eos.security.impl.service.internal.TransactionManagerImpl;

/**
 * @author fabiano.santos
 * 
 */
public class SchemaUtil {

	private static final Logger log = LoggerFactory.getLogger(SchemaUtil.class);

	public static void createSchema() {
		TransactionManagerImpl manager = TransactionManagerImpl.transactionManager();
		manager.begin();
		try {
			Schema schema = manager.graphDB().schema();
			createConstraint(schema, EOSTenantDAO.label, CollectionUtil.asSet("alias"));
			createIndex(schema, EOSTenantDAO.label, CollectionUtil.asSet("state"));
			createIndex(schema, EOSTenantDataDAO.label, CollectionUtil.asSet("key"));
		} finally {
			manager.commit();
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
