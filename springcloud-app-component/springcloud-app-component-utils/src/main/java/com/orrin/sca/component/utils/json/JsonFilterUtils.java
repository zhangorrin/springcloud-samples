package com.orrin.sca.component.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class JsonFilterUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(JsonFilterUtils.class);

	public static String jsonFilter(Object object, JsonFilterInfo[] jsonFilterInfos){
		CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();

		for(JsonFilterInfo jsonFilterInfo : jsonFilterInfos){
			jsonSerializer.filter(jsonFilterInfo.getType(),jsonFilterInfo.getInclude(),jsonFilterInfo.getFilter());

		}

		try {
			String json = jsonSerializer.toJson(object);
			return json;
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonFilterUtils error,{},{}",object.toString(),jsonFilterInfos.toString());
			e.printStackTrace();
			return null;
		}
	}

	public static String jsonFilter(Object object, JsonFilterInfo jsonFilterInfo){
		JsonFilterInfo[] jsonFilterInfos = new JsonFilterInfo[1];
		jsonFilterInfos[0] = jsonFilterInfo;
		return jsonFilter(object,jsonFilterInfos);
	}
}