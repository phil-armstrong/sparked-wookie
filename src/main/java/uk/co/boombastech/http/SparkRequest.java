package uk.co.boombastech.http;

import spark.QueryParamsMap;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.emptyList;

public class SparkRequest implements Request {

	private final spark.Request wrappedRequest;

	public SparkRequest(spark.Request wrappedRequest) {
		this.wrappedRequest = wrappedRequest;
	}

	@Override
	public List<String> getQueryParameter(Parameter parameter) {
		QueryParamsMap queryParamsMap = wrappedRequest.queryMap(parameter.name());
		if (queryParamsMap.hasValue()) {
			return newArrayList(queryParamsMap.values());
		}
		return emptyList();
	}

	@Override
	public Map<Parameter, List<String>> getAllQueryParameters() {
		Map<Parameter, List<String>> queryParams = newHashMap();
		QueryParamsMap queryParamsMap = wrappedRequest.queryMap();
		Map<String, String[]> stringMap = queryParamsMap.toMap();

		for (String key : stringMap.keySet()) {
			try {
				Parameter parameter = Parameter.valueOf(key);
				List<String> values = newArrayList();
				for (String value : stringMap.get(key)) {
					values.add(value);
				}
				queryParams.put(parameter, values);
			}
			catch (IllegalArgumentException illegalArgumentException) {}
		}

		return queryParams;
	}
}