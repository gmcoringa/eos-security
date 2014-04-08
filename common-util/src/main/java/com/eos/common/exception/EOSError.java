/**
 * 
 */
package com.eos.common.exception;

import java.io.Serializable;

/**
 * Class with detailed error.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSError implements Serializable {

	private static final long serialVersionUID = -4527388598739928510L;
	private final Integer code;
	private final String message;

	/**
	 * Default constructor.
	 * 
	 * @param code
	 * @param message
	 */
	public EOSError(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EOSError other = (EOSError) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EOSError [code=" + code + ", message=" + message + "]";
	}

}
