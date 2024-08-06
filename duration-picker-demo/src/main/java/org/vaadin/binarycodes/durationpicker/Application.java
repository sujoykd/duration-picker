package org.vaadin.binarycodes.durationpicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "duration-picker")
public class Application implements AppShellConfigurator {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
