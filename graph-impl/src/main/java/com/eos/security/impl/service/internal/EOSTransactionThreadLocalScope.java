package com.eos.security.impl.service.internal;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.Scope;

import com.eos.security.impl.service.DataBaseServer;
import com.eos.security.impl.service.TransactionManager;

public class EOSTransactionThreadLocalScope implements Scope {
	
	public static final String SCOPE_NAME = "threadLocal";
	
	private ThreadLocal<TransactionManager> instance = new ThreadLocal<>();

	private final DataBaseServer dataBaseServer;
	
	@Autowired
	public EOSTransactionThreadLocalScope(DataBaseServer dataBaseServer){
		this.dataBaseServer = dataBaseServer;
	}

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		TransactionManager transactionManager = instance.get();
		
		if(transactionManager == null){
			transactionManager = new TransactionManagerImpl(dataBaseServer);
			instance.set(transactionManager);
		}
		
		return transactionManager;
	}

	@Override
	public Object remove(String name) {
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// Nothing

	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}

}
