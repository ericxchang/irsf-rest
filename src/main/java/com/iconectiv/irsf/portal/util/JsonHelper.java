package com.iconectiv.irsf.portal.util;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iconectiv.irsf.json.vaidation.JsonValidationException;
import com.iconectiv.irsf.json.vaidation.JsonValidator;

public class JsonHelper {
	private static Logger log = LoggerFactory.getLogger(JsonHelper.class);

	private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Error to convert object to json:", e);
		}
		
		return "";
	}

	public static String toPrettyJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Error to convert object to json:", e);
		}
		
		return "";
	}

	public static <T> T  fromJson(String json, Class<T> classOfT, boolean skipValidation) throws JsonValidationException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (skipValidation) {
				return mapper.readValue(json, classOfT);
			} else {
				return new JsonValidator().validate( mapper.readValue(json, classOfT) );
			}
		} catch (JsonParseException e) {
			log.error("Error to parse json:", e);
		} catch (JsonMappingException e) {
			log.error("Error to parse json:", e);
		} catch (IOException e) {
			log.error("Error to parse json:", e);
		} catch (JsonValidationException e) {
			log.error("Input json is invalid: " +  e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error to parse json:", e);
		}
		
		return null;
	}
	
	
	public static <T> T  fromJson(String json, Class<T> classOfT) throws JsonValidationException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return new JsonValidator().validate( mapper.readValue(json, classOfT) );
		} catch (JsonParseException e) {
			log.error("Error to parse json:", e);
		} catch (JsonMappingException e) {
			log.error("Error to parse json:", e);
		} catch (IOException e) {
			log.error("Error to parse json:", e);
		} catch (JsonValidationException e) {
			log.error("Input json is invalid: " +  e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error to parse json:", e);
		}
		
		return null;
	}
	
	
	public static <T> T  fromJson(String json, String elementKey, Class<T> classOfT) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonObj = mapper.readTree(json).get(elementKey);
			return mapper.readValue(jsonObj.toString(), classOfT);
		} catch (JsonParseException e) {
			log.error("Error to parse json:", e);
		} catch (JsonMappingException e) {
			log.error("Error to parse json:", e);
		} catch (IOException e) {
			log.error("Error to parse json:", e);
		}
		
		
		return null;
	}
	
	
	
	public static void toFile(Object object, String fileName) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), object);
		} catch (Exception e) {
			log.error("Error to write to file:", e);
		}
		return;
	}
	
	public static void toFileInByte(Object object, String fileName) {
		try {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encVal = c.doFinal(toPrettyJson(object).getBytes());
	        byte[] encryptedValue = Base64.getEncoder().encode(encVal);
	        FileUtils.writeByteArrayToFile(new File(fileName), encryptedValue);
		} catch (NoSuchAlgorithmException e) {
			log.error("Error to write to file:", e);
		} catch (IOException e) {
			log.error("Error to write to file:", e);
		} catch (Exception e) {
			log.error("Error to write to file:", e);
		}
		return;
	}
	

	public static String decodeByteToString(String fileName) {
		try {
			
			byte[] data = FileUtils.readFileToByteArray(new File(fileName));
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = Base64.getDecoder().decode(data);
	        byte[] decValue = c.doFinal(decordedValue);
	        String decryptedValue = new String(decValue);
	        return decryptedValue;
		} catch (Exception e) {
			log.error("Error to write to file:", e);
		}
		return null;
	}
	
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }	
}
