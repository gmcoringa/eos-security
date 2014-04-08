/**
 * 
 */
package com.eos.common;

/**
 * Enumerator to indicate accessibility level.
 * 
 * @author fabiano.santos
 * 
 */
public enum EOSLevel {
	/**
	 * Maximum level, limits the maximum level of an entity.
	 */
	MAXIMUM(20000),
	/**
	 * Public minimum level. Any number between this and
	 * {@link EOSLevel#MAXIMUM} is considered public.
	 */
	PUBLIC(5001),
	/**
	 * Reserved maximum level. Any number between {@link EOSLevel#PRIVATE} and
	 * this is considered reserved.
	 * 
	 */
	RESERVED(5000),
	/**
	 * Private maximum level. Any number between {@link EOSLevel#INTERNAL} and
	 * this is considered private.
	 */
	PRIVATE(1000),
	/**
	 * Internal maximum level. Any number between 0 and this is considered
	 * internal.
	 */
	INTERNAL(100);

	private final Integer level;

	private EOSLevel(Integer level) {
		this.level = level;
	}

	/**
	 * Get default value for this level.
	 * 
	 * @return Default level value.
	 */
	public Integer getLevel() {
		return level;
	}
}
