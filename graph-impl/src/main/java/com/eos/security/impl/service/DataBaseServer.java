/**
 * 
 */
package com.eos.security.impl.service;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * @author fabiano.santos
 * 
 */
public interface DataBaseServer {

	/**
	 * Retrieve graph database instance.
	 */
	public GraphDatabaseService get();
}
