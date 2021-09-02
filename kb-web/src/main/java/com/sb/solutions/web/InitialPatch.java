package com.sb.solutions.web;

import java.io.File;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * @author Rujan Maharjan on 7/12/2019
 */

@Component
public final class InitialPatch {

    private static final String GENERALPATCHFOLDER = "/general_patch";

    private static final Logger logger = LoggerFactory.getLogger(InitialPatch.class);

    private InitialPatch() {
    }

    public static void inital(String baseServerFolder, DataSource dataSource) {
        ClassPathResource dataResource = new ClassPathResource(
            baseServerFolder + GENERALPATCHFOLDER + File.separator + "location.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(dataResource);

        populator.execute(dataSource);

        logger.info("executing patch query {}", baseServerFolder);

        ClassPathResource dataResource3 = new ClassPathResource(
            baseServerFolder + GENERALPATCHFOLDER + File.separator + "patch.sql");
        ResourceDatabasePopulator populator3 = new ResourceDatabasePopulator(dataResource3);

        populator3.execute(dataSource);


    }

}




