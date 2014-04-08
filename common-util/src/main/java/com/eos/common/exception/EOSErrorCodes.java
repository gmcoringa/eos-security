/**
 * 
 */
package com.eos.common.exception;

/**
 * All known error codes for security module.
 * 
 * @author santos.fabiano
 * 
 */
public interface EOSErrorCodes {

	Integer REQUIRED = 1;
	Integer MAX_SIZE = 2;
	Integer MIN_SIZE = 3;
	Integer INVALID_CHARS = 4;

	Integer INVALID_LOGIN = 11;
	Integer INVALID_EMAIL = 12;
	Integer INVALID_PASSWORD = 13;
	Integer INVALID_STATE = 14;

	Integer GENERIC = 500;
}
