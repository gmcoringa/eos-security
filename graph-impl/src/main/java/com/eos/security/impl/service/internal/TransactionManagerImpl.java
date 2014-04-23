/**
 * 
 */
package com.eos.security.impl.service.internal;

import java.util.concurrent.atomic.AtomicInteger;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;

import com.eos.security.api.service.TransactionManager;

/**
 * @author fabiano.santos
 * 
 */
public class TransactionManagerImpl implements TransactionManager {

	private static ThreadLocal<TransactionManager> instance = new ThreadLocal<>();

	private final GraphDatabaseService graphDB;
	private final ExecutionEngine engine;
	private Transaction transaction;
	private final AtomicInteger transactionCounter = new AtomicInteger();

	public TransactionManagerImpl() {
		graphDB = DataBaseServer.get();
		engine = new ExecutionEngine(graphDB, StringLogger.SYSTEM);
	}

	public GraphDatabaseService graphDB() {
		return graphDB;
	}

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

	public static synchronized TransactionManager get() {
		TransactionManager tm = instance.get();
		if (tm == null) {
			tm = new TransactionManagerImpl();
			instance.set(tm);
		}

		return tm;
	}

	/**
	 * Retrieve transaction manager internal implementation instance.
	 * 
	 * @return TransactionManager implementation.
	 */
	public static TransactionManagerImpl transactionManager() {
		return (TransactionManagerImpl) TransactionManagerImpl.get();
	}

}
