package com.orrin.sca.component.utils.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.orrin.sca.component.utils.date.DatePattern;
import com.orrin.sca.component.utils.json.datetime.DateTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class JacksonUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

	private final static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new DateTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS.getValue()));
	}

	private JacksonUtils() {
	}

	public static String encode(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			logger.error("encode(Object)", e); //$NON-NLS-1$
		} catch (JsonMappingException e) {
			logger.error("encode(Object)", e); //$NON-NLS-1$
		} catch (IOException e) {
			logger.error("encode(Object)", e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * 将json string反序列化成对象
	 *
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T decode(String json, Class<T> valueType) {
		try {
			return objectMapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			logger.error("decode(String, Class<T>)", e);
		} catch (JsonMappingException e) {
			logger.error("decode(String, Class<T>)", e);
		} catch (IOException e) {
			logger.error("decode(String, Class<T>)", e);
		}
		return null;
	}

	/**
	 * 将json array反序列化为对象
	 *
	 * @param json
	 * @param typeReference
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T decode(String json, TypeReference<T> typeReference) {
		try {
			return (T) objectMapper.readValue(json, typeReference);
		} catch (JsonParseException e) {
			logger.error("decode(String, JsonTypeReference<T>)", e);
		} catch (JsonMappingException e) {
			logger.error("decode(String, JsonTypeReference<T>)", e);
		} catch (IOException e) {
			logger.error("decode(String, JsonTypeReference<T>)", e);
		}
		return null;
	}
}
