/**
 * 
 */
package com.eos.security.impl.service.internal;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eos.security.impl.service.DataBaseServer;

/**
 * shutdown listener. Close graph database instance.
 * 
 * @author fabiano.santos
 * 
 */
@Component("RemoteDataBaseServer")
public class RemoteDataBaseServer implements DataBaseServer {

	private GraphDatabaseService graphService;
	private static final Logger log = LoggerFactory.getLogger(RemoteDataBaseServer.class);

	@PostConstruct
	public void init() {
		log.info("### Initializing Graph database server ###");
		/// TODO graphService = ??
		log.info("### Graph database server UP ###");
	}

	@PreDestroy
	public void shutdown() {
		log.info("### Shutingdown Graph database ###");
		graphService.shutdown();
		log.info("### Graph database shutdown complete ###");
	}

	@Override
	public GraphDatabaseService get() {
		return graphService;
	}
}
