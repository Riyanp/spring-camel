package com.odenktools.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RequestUtils {

	public static ObjectNode getAllParams(HttpServletRequest httpServletRequest) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		Map m = httpServletRequest.getParameterMap();
		Set s = m.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
			String key = entry.getKey();
			String[] value = entry.getValue();
			objectNode.put(key, value[0]);
		}
		return objectNode;
	}
}
