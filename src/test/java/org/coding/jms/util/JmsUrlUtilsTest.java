package org.coding.jms.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsUrlUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(JmsUrlUtilsTest.class);

	@Test
	public void testEncoding() {
		Map<String, String> tcpParamMap = new HashMap<String, String>();
		tcpParamMap.put("wireFormat.maxInactivityDurationInitalDelay", "30000");
		String url = JmsUrlUtils.createTcpUrl("www.google.com.tw", 61616, tcpParamMap);
		logger.debug(url);
		Map<String, String> failoverParamMap = new HashMap<String, String>();
		failoverParamMap.put("maxReconnectDelay", "10000");
		failoverParamMap.put("timeout", "3000");
		logger.debug(JmsUrlUtils.createFailoverUrl(url, failoverParamMap));
	}

}
