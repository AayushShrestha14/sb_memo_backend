package com.sb.solutions.web;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sb.solutions.api.basehttp.BaseHttp;
import com.sb.solutions.api.basehttp.BaseHttpRepo;
import com.sb.solutions.api.user.repository.UserRepository;
import com.sb.solutions.core.config.security.SpringSecurityAuditorAware;
import com.sb.solutions.core.config.security.property.FileStorageProperties;
import com.sb.solutions.core.config.security.property.MailProperties;
import com.sb.solutions.core.constant.CurrentDbServer;

/**
 * @author Rujan Maharjan on 12/27/2018
 */
@SpringBootApplication(scanBasePackages = "com.sb.solutions")
@ComponentScan(basePackages = "com.sb.solutions")
@EnableJpaRepositories(basePackages = "com.sb.solutions")
@EntityScan(basePackages = "com.sb.solutions")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableConfigurationProperties({FileStorageProperties.class, MailProperties.class})
@EnableScheduling
public class CpSolutionApplication extends SpringBootServletInitializer {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BaseHttpRepo baseHttpRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;

    @Value("${server.port}")
    private String port;

    @Value("${spring.datasource.url}")
    private String dbValue;


    public static void main(String[] args) {
        SpringApplication.run(CpSolutionApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CpSolutionApplication.class);
    }


    @PostConstruct
    public void initialize() {
        String baseServerFolder = CurrentDbServer.currentConnectedDb(dbValue);
        InitialPatch.inital(baseServerFolder, dataSource);
        if (baseHttpRepo.findAll().isEmpty()) {
            BaseHttp baseHttp = new BaseHttp();
            baseHttp.setBaseUrl("http://" + baseHttp.getHostAddress() + ":" + port + "/");
            baseHttp.setFlag(1);
            baseHttpRepo.save(baseHttp);
        }
        ClassPathResource dataResourceGeneral = new ClassPathResource(
            baseServerFolder + "/loan_sql/patch_general_permission.sql");
        ResourceDatabasePopulator populatorGeneral = new ResourceDatabasePopulator(
            dataResourceGeneral);
        populatorGeneral.execute(dataSource);

        ClassPathResource dataResource = new ClassPathResource(
            baseServerFolder + "/loan_sql/patch_credit_memo.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
            dataResource);
        populator.execute(dataSource);

    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new SpringSecurityAuditorAware();
    }
}
