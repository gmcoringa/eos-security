/**
 * 
 */
package com.eos.security.impl.listener;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.schema.Schema;

import com.eos.security.impl.service.internal.TransactionManagerImpl;

/**
 * @author fabiano.santos
 * 
 */
public class SchemaUtil {

	public static void createSchema() {
		TransactionManagerImpl manager = TransactionManagerImpl.transactionManager();
		manager.begin();
		try {
			Schema schema = manager.graphDB().schema();
			schema.constraintFor(DynamicLabel.label("Tenant")).assertPropertyIsUnique("alias").create();
		} finally {
			manager.commit();
		}
	}
}
