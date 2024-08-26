package org.cftoolsuite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@SpringBootApplication
@ConfigurationPropertiesScan
@Theme(themeClass = Material.class, variant = Material.DARK)
@PWA(name = "A simple user interface to interact with a robert instance", shortName = "robert-ui")
public class RobertUiApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(RobertUiApplication.class, args);
	}

}
