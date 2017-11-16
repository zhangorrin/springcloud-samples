package com.orrin.sca.component.utils.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.orrin.sca.component.utils.json.annotation.JSON;
import com.orrin.sca.component.utils.json.datetime.DateTimeModule;
import com.orrin.sca.component.utils.json.filter.JacksonJsonFilter;
import org.springframework.util.StringUtils;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class CustomerJsonSerializer {


	ObjectMapper mapper = new ObjectMapper();
	JacksonJsonFilter jacksonFilter = new JacksonJsonFilter();

	@SuppressWarnings("unchecked")
	public void filter(Class<?> clazz, String include, String filter) {
		if (clazz == null) return;
		if (!StringUtils.isEmpty(include)) {
			jacksonFilter.include(clazz, include.split(","));
		}
		if (!StringUtils.isEmpty(filter)) {
			jacksonFilter.filter(clazz, filter.split(","));
		}
		mapper.addMixInAnnotations(clazz, jacksonFilter.getClass());
	}

	@SuppressWarnings("unchecked")
	public String toJson(Object object) throws JsonProcessingException {
		mapper.setFilters(jacksonFilter);
		mapper.registerModule(new DateTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper.writeValueAsString(object);
	}
	public void filter(JSON json) {
		this.filter(json.type(), json.include(), json.filter());
	}
}