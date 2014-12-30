/**
 * 
 */
package com.eos.security.impl.service.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eos.common.exception.EOSException;
import com.eos.security.impl.service.DataBaseServer;

/**
 * shutdown listener. Close graph database instance.
 * 
 * @author fabiano.santos
 * 
 */
@Component("EmbeddedDataBaseServer")
public class EmbeddedDataBaseServer implements DataBaseServer {

	private GraphDatabaseService graphService;
	@Value("${database.rootDirectory}")
	private String databaseDir;
	@Value("${database.clean}")
	private String clean;
	private static final Logger LOG = LoggerFactory.getLogger(EmbeddedDataBaseServer.class);

	private void createPath() {
		try {
			Path dir = Paths.get(databaseDir);
			LOG.info("Database root directory [{}]", dir.toAbsolutePath().toString());
			Files.createDirectories(dir);
		} catch (IOException e) {
			LOG.debug("Failed to create database directory for path {}", databaseDir);
			throw new EOSException("Failed to create database directory for path", e);
		}

	}

	@PostConstruct
	public void init() {
		LOG.info("### Initializing Embedded Graph database server ###");
		createPath();
		graphService = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDir);

		if (Boolean.parseBoolean(clean)) {
			cleanDatabase();
		}

		LOG.info("### Graph database server UP ###");
	}

	private void cleanDatabase() {
		LOG.info("Cleaning database data!");
		ExecutionEngine engine = new ExecutionEngine(graphService, StringLogger.SYSTEM);

		try (Transaction transaction = graphService.beginTx()) {
			engine.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");
			transaction.success();
		}
	}

	@PreDestroy
	public void shutdown() {
		LOG.info("### Shutingdown Graph database ###");
		graphService.shutdown();
		LOG.info("### Graph database shutdown complete ###");
	}

	@Override
	public GraphDatabaseService get() {
		return graphService;
	}
}
