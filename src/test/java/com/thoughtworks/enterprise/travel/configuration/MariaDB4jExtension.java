package com.thoughtworks.enterprise.travel.configuration;

import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Objects;

@Slf4j
public class MariaDB4jExtension implements BeforeAllCallback, AfterAllCallback {

    private static final int DEFAULT_PORT = 60000;
    private static MariaDB4jSpringService mariaDB4jSpringService;

    @Override
    public void afterAll(ExtensionContext context) {

    }

    @Override
    public void beforeAll(ExtensionContext context) {
        if (Objects.nonNull(mariaDB4jSpringService)) {
            return;
        }

        MariaDB4jSpringService db4jSpringService = new MariaDB4jSpringService();
        DBConfigurationBuilder configuration = db4jSpringService.getConfiguration();
        configuration.addArg("--character-set-server=utf8");
        configuration.addArg("--lower_case_table_names=1");
        configuration.addArg("--collation-server=utf8_general_ci");
        configuration.addArg("--max-connections=1024");
        configuration.addArg("--user=root");

        db4jSpringService.setDefaultPort(DEFAULT_PORT);

        try {
            db4jSpringService.start();
            db4jSpringService.getDB().createDB("travel_db");
            MariaDB4jExtension.mariaDB4jSpringService = db4jSpringService;
            log.info("started new mariadb4j instance ");
        } catch (Exception e) {
            if (log != null) {
                log.error("failed to start mariadb4j", e);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                MariaDB4jExtension.mariaDB4jSpringService.stop();
            } catch (Exception e) {
                if (log != null) {
                    log.error("failed to stop mariadb4j", e);
                }
            }
        }));
    }
}
