package com.eos.security.impl.test.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.eos.security.impl.service.DataBaseServer;

public class TestDataBaseServer implements DataBaseServer {

	private GraphDatabaseService graphDatabaseService;
	
	@PostConstruct
	public void init() {
		graphDatabaseService = new TestGraphDatabaseFactory().newImpermanentDatabase();

	}

	@PreDestroy
	public void shutdown() {
		graphDatabaseService.shutdown();
	}

	@Override
	public GraphDatabaseService get() {
		return graphDatabaseService;
	}

}
