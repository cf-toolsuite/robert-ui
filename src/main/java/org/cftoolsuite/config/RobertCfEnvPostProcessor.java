package org.cftoolsuite.config;

import java.util.Map;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfService;
import io.pivotal.cfenv.spring.boot.CfEnvProcessor;
import io.pivotal.cfenv.spring.boot.CfEnvProcessorProperties;

public class RobertCfEnvPostProcessor implements CfEnvProcessor {

	private static final String SERVICE_NAME = "robert-config";

    @Override
	public boolean accept(CfService service) {
		return service.existsByLabelStartsWith("user-provided");
	}

	@Override
	public void process(CfCredentials cfCredentials, Map<String, Object> properties) {
		Map<String, Object> allCredentials = cfCredentials.getMap();
		for (Map.Entry<String, Object> entry : allCredentials.entrySet()) {
			properties.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public CfEnvProcessorProperties getProperties() {
		return CfEnvProcessorProperties.builder()
				.serviceName(SERVICE_NAME)
				.build();
	}
}
