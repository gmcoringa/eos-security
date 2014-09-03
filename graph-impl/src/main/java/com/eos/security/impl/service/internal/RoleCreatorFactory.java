/**
 * 
 */
package com.eos.security.impl.service.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.eos.common.exception.EOSException;

/**
 * Factory for load role resources.
 * 
 * @author santos.fabiano
 * 
 */
public class RoleCreatorFactory {

	private static final ObjectMapper mapper;
	private static final TypeReference<List<RoleCreator>> reference = new TypeReference<List<RoleCreator>>() {
	};

	static {
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
	}

	public static List<RoleCreator> getRoles() {
		InputStream resource = RoleCreatorFactory.class.getResourceAsStream("/defaultRolePermissions.json");
		try {
			List<RoleCreator> roles = mapper.readValue(resource, reference);
			return roles;
		} catch (IOException e) {
			throw new EOSException("Failed to parse json", e);
		}
	}
}
