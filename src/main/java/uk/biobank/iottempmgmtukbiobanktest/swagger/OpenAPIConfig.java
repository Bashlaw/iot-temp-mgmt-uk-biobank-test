package uk.biobank.iottempmgmtukbiobanktest.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing the OpenAPI documentation.
 * This class sets metadata information specific to the UK Bio-Bank IoT
 * Temperature Record Backend APIs.
 * <p>
 * It provides a bean that customizes the OpenAPI instance with details
 * such as title, description, version, and license information.
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("UK Bio-Bank IoT Temperature Record Backend APIs")
                        .description("A UK Bio-Bank IoT Temperature Record Backend APIs code challenge")
                        .version("v0.0.1").license(new License().name("Apache 2.0")
                                .url("https://biobank.uk")));
    }

}
