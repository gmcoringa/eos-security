/**
 * 
 */
package com.eos.common;

/**
 * Enumerator indicating an entity state.
 * 
 * @author santos.fabiano
 * 
 */
public enum EOSState {
	/**
	 * Entity not verified. Its is considered disabled, is active but is not
	 * accessible or cannot perform any action.
	 */
	INACTIVE,
	/**
	 * Entity active, already verified.
	 */
	ACTIVE,
	/**
	 * Entity disabled, also known as logical deletion. Its is considered
	 * disabled, is active but is not accessible or cannot perform any action.
	 */
	DISABLED;
}
