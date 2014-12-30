/**
 * 
 */
package com.eos.security.impl.transaction;

import java.util.concurrent.atomic.AtomicInteger;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.TransactionManager;
import com.eos.security.impl.service.internal.EOSTransactionThreadLocalScope;

/**
 * @author fabiano.santos
 * 
 */
@Component
@Scope(value = EOSTransactionThreadLocalScope.SCOPE_NAME, proxyMode = ScopedProxyMode.INTERFACES)
public class TransactionManagerImpl implements TransactionManager {

	private final GraphDatabaseService graphDB;
	private final ExecutionEngine engine;
	private final AtomicInteger transactionCounter = new AtomicInteger();
	private Transaction transaction;

	private final DataBaseServer dataBaseServer;

	@Autowired
	public TransactionManagerImpl(@Qualifier("${database.mode}") DataBaseServer server) {
		dataBaseServer = server;
		graphDB = dataBaseServer.get();
		engine = new ExecutionEngine(graphDB, StringLogger.SYSTEM);
	}

	@Override
	public GraphDatabaseService graphDB() {
		return graphDB;
	}

	@Override
	public ExecutionEngine executionEngine() {
		return engine;
	}

	/**
	 * @see com.eos.security.api.service.TransactionManager#begin()
	 */
	@Override
	public TransactionManager begin() {
		if (!isOpen()) {
			transaction = graphDB.beginTx();
		}

		transactionCounter.incrementAndGet();
		return this;
	}

	/**
	 * @see com.eos.security.api.service.TransactionManager#commit()
	 */
	@Override
	public TransactionManager commit() {
		if (isOpen()) {
			if (transactionCounter.get() == 1) {
				try {
					transaction.success();
					transactionCounter.set(0);
				} finally {
					transaction.close();
					transaction = null;
				}
			} else {
				transactionCounter.decrementAndGet();
			}
		}

		return this;
	}

	/**
	 * @see com.eos.security.api.service.TransactionManager#rollback()
	 */
	@Override
	public TransactionManager rollback() {
		if (isOpen()) {
			try {
				transaction.failure();
				transactionCounter.set(0);
			} finally {
				transaction.close();
				transaction = null;
			}
		}

		return this;
	}

	/**
	 * @see com.eos.security.api.service.TransactionManager#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return transactionCounter.get() > 0;
	}

	@Override
	public void close() throws Exception {
		commit();

	}

}
