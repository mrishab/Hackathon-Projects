package com.sap.orca.starter.sample.jaxrs;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.sap.orca.starter.core.config.ApplicationConfig;
import com.sap.orca.starter.core.security.AuthenticationType;
import com.sap.orca.starter.core.security.SecurityConfig;
import com.sap.orca.starter.core.security.SecurityConfigBuilder;
import com.sap.orca.starter.data.ds.ConnectionProperties;
import com.sap.orca.starter.data.ds.DataSourcePool;
import com.sap.orca.starter.data.ds.IDataSourceBuilder;
import com.sap.orca.starter.data.ds.SingleDbDataSourcePool;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class JaxRsApp extends SpringBootServletInitializer {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationConfig appConfig() {
        return new ApplicationConfig() {
            @Override
            public String getApplicationName() {
                return "sampleallfeatures2";
            }
        };
    }
    
    @Bean
    public DataSourcePool connectionPool(ApplicationConfig config, IDataSourceBuilder dataSourceBuilder) {
        DataSource ds = dataSourceBuilder.createDataSource(null);
        ConnectionProperties properties = ConnectionProperties.Builder
                .create(config)
                .dataSource(ds)
                .build();
        return new SingleDbDataSourcePool(properties);
    }
    
  

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityConfig securityConfig() {
        return SecurityConfigBuilder.builder()
                                    .protect("/api/**", AuthenticationType.None)
                                    .permit("/health", "/api/swagger.json")
                                    .build();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(JaxRsApp.class).run(args);
    }
}
