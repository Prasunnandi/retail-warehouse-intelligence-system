package com.retailintelligence.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI retailWarehouseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Retail & Warehouse Intelligence System REST API")
                        .description("Unified Enterprise Platform merging StoreOps, WarehouseIQ, StockAlert, and Retail Profit Detective.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Prasun Nandi")
                                .email("prasunnandi18@gmail.com")
                                .url("https://github.com/Prasunnandi/retail-warehouse-intelligence-system"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
