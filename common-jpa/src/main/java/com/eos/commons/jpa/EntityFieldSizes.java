/**
 * 
 */
package com.eos.commons.jpa;

/**
 * Generic fields size.
 * 
 * @author santos.fabiano
 * 
 */
public interface EntityFieldSizes {

	int MINIMUM = 2;
	int KEY = 127;
	// Data fields
	int DATA_TINY = 63;
	int DATA_SMALL = 127;
	int DATA_STANDART = 255;
	int DATA_LARGE = 511;
	int DATA_HUGE = 1023;
	// Buffers
	int BUF_SMALL = 1023;
	int BUF_STANDART = 2047;
	int BUF_LARGE = 4000; // 500KB

	// BLOB SIZES
	int BLOB = 65536; // 64 KB
	int MEDIUM_BLOB = 16777216; // 16 MB
	long LONG_BLOB = 4294967296L; // 4 GB

	// Photo sizes
	short THUMBNAIL_TINY_WIDTH = 32;
	short THUMBNAIL_TINY_HEIGTH = 32;
	short THUMBNAIL_MINI_WIDTH = 96;
	short THUMBNAIL_MINI_HEIGTH = 96;
	short THUMBNAIL_MEDIUM_WIDTH = 150;
	short THUMBNAIL_MEDIUM_HEIGTH = 150;
	short THUMBNAIL_LARGE_WIDTH = 770;
	short THUMBNAIL_LARGE_HEIGTH = 578;

	// Query fields
	int MAXIMUM_LIMIT = 250;
	String MAXIMUM_LIMIT_STR = String.valueOf(MAXIMUM_LIMIT);
	// Constant indicating when the limit must not be configured.
	int UNLIMITED = -2;
	
	int EMAIL_MIN_SIZE = 5;
	int PASSWORD_MIN_SIZE = 6;
}
