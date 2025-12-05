package com.df.fne.core.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfiguration {

     @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("DOT LONACI API")
                        .version("1.0")
                        .description("La documentation de l'api dot lonaci")
                        .contact(new Contact().name("DIGIN FACTORY"))
                        //.termsOfService("https://www.diginfactory.com")
                        .license(new License().name("DIGIN FACTORY").url("https://www.diginfactory.com")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
