package com.workspark.authenticationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilityMethod {
	
	 public static String asJsonString(final Object obj) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.writeValueAsString(obj);
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to convert object to JSON", e);
	        }
	 }

}
