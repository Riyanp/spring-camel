package com.odenktools.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.odenktools.common.model")
public class MasterApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MasterApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(MasterApplication.class, args);
        LOG.info("APPLICATION IS RUNNING!");
    }
}
