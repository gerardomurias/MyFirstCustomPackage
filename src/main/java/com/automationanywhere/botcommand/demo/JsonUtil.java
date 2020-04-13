package com.automationanywhere.botcommand.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class JsonUtil {

	public static Map<String, Object> toMap(String jsonString) throws IOException {
		return new ObjectMapper().readValue(jsonString, HashMap.class);
	}

	public static String toJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return objectMapper.writeValueAsString(object);
	}

	public static String fromJson(String jsonString) throws IOException {
		return new ObjectMapper().readValue(jsonString, String.class);
	}
}
