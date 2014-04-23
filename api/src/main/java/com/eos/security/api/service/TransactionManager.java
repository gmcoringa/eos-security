/**
 * 
 */
package com.eos.security.api.service;

/**
 * Transaction manager controller. Used to control transactions in situations where container transaction control is not
 * possible.
 * 
 * @author fabiano.santos
 * 
 */
public interface TransactionManager {

	/**
	 * Begin a transaction. If there is a transaction open, nothing is done.
	 * 
	 * @return The transaction manager instance.
	 */
	public TransactionManager begin();

	/**
	 * Commit an open transaction. A transaction is not committed if was opened multiple times.
	 * 
	 * @return The transaction manager instance.
	 */
	public TransactionManager commit();

	/**
	 * Rollback a transaction.
	 * 
	 * @return The transaction manager instance.
	 */
	public TransactionManager rollback();

	/**
	 * Verify if there is an open transaction.
	 * 
	 * @return True if there is an open transaction, False otherwise.
	 */
	public boolean isOpen();
}
