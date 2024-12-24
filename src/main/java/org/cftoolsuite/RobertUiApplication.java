package org.cftoolsuite;

import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@Push
@SpringBootApplication
@EnableFeignClients
@EnableReactiveFeignClients
@ConfigurationPropertiesScan
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@PWA(name = "A simple user interface to interact with a robert instance", shortName = "robert-ui")
public class RobertUiApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(RobertUiApplication.class, args);
	}

}
