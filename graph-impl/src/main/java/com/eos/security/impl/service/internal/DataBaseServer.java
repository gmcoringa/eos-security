/**
 * 
 */
package com.eos.security.impl.service.internal;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shutdown listener. Close graph database instance.
 * 
 * @author fabiano.santos
 * 
 */
public class DataBaseServer {

	private static GraphDatabaseService graphService;
	private static final Logger log = LoggerFactory.getLogger(DataBaseServer.class);

	public static void init() {
		log.info("### Initializing Graph database server ###");
		graphService = new GraphDatabaseFactory().newEmbeddedDatabase("target/graph");
		log.info("### Graph database server UP ###");
	}

	public static void shutdown() {
		graphService.shutdown();
		log.info("### Graph database shutdown complete ###");
	}

	public static GraphDatabaseService get() {
		return graphService;
	}
}
