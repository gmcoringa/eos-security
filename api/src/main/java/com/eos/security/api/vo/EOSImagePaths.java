/**
 * 
 */
package com.eos.security.api.vo;

import java.io.Serializable;

/**
 * Generic class for images with various sizes.
 * 
 * @author santos.fabiano
 * 
 */
public class EOSImagePaths implements Serializable {

	private static final long serialVersionUID = 592701850131109876L;
	private String thumbURL;
	private String photoSmallURL;
	private String photoStandardURL;

	/**
	 * Default constructor.
	 */
	private EOSImagePaths() {
		super();
	}

	/**
	 * @return the thumbURL
	 */
	public String getThumbURL() {
		return thumbURL;
	}

	/**
	 * @param thumbURL
	 *            the thumbURL to set
	 */
	public void setThumbURL(String thumbURL) {
		this.thumbURL = thumbURL;
	}

	/**
	 * @return the photoSmallURL
	 */
	public String getPhotoSmallURL() {
		return photoSmallURL;
	}

	/**
	 * @param photoSmallURL
	 *            the photoSmallURL to set
	 */
	public void setPhotoSmallURL(String photoSmallURL) {
		this.photoSmallURL = photoSmallURL;
	}

	/**
	 * @return the photoStandardURL
	 */
	public String getPhotoStandardURL() {
		return photoStandardURL;
	}

	/**
	 * @param photoStandardURL
	 *            the photoStandardURL to set
	 */
	public void setPhotoStandardURL(String photoStandardURL) {
		this.photoStandardURL = photoStandardURL;
	}

}
