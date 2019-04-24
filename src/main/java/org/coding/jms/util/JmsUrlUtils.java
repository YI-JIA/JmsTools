package org.coding.jms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsUrlUtils {

	private static final Logger logger = LoggerFactory.getLogger(JmsUrlUtils.class);

	private static final String URI_ENCODING = "ISO-8859-1";

	public static String createTcpUrl(String hostname, int port, Map<String, String> parameters) {

		checkEmptyString(hostname, "hostname can not be empty");

		StringBuilder tcpUrl = new StringBuilder();
		tcpUrl.append("tcp://");
		tcpUrl.append(hostname);
		tcpUrl.append(":");
		tcpUrl.append(port);
		tcpUrl.append(createParamUrl(parameters));
		return tcpUrl.toString();
	}

	public static String createFailoverUrl(String url, Map<String, String> parameters) {

		checkEmptyString(url, "URL can not be empty");

		StringBuilder failoverUrl = new StringBuilder();
		failoverUrl.append("failover:(");
		failoverUrl.append(url);
		failoverUrl.append(")");
		failoverUrl.append(createParamUrl(parameters));
		return failoverUrl.toString();
	}

	public static String createParamUrl(Map<String, String> parameters) {
		StringBuilder paramUrl = new StringBuilder();
		int count = 0;
		if (parameters != null && !parameters.isEmpty()) {
			for (Entry<String, String> param : parameters.entrySet()) {
				try {
					paramUrl.append(((count == 0) ? "?" : "&"))
					        .append(URLEncoder.encode(param.getKey(), URI_ENCODING))
							.append("=")
							.append(URLEncoder.encode(param.getValue(), URI_ENCODING));
					count++;
				} catch (UnsupportedEncodingException e) {
					// skip this parameter url
					logger.warn("URI encoding failed", e);
				}
			}
		}
		return paramUrl.toString();
	}

	private static void checkEmptyString(String value, String errorMsg) throws IllegalArgumentException {
		if (value == null || value.isEmpty())
			throw new IllegalArgumentException(errorMsg);
	}
}
