package uk.biobank.iottempmgmtukbiobanktest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    /**
     * Configures the security filter chain for the application.
     * The method disables CSRF protection, enables Cross-Origin Resource Sharing (CORS)
     * with default settings, and specifies authorization rules for HTTP requests.
     * Certain endpoints such as Swagger UI, actuator, and temperature record APIs
     * are permitted access without authentication while others require authentication.
     *
     * @param http the HttpSecurity object used to configure web-based security
     * @return a SecurityFilterChain representing the configured security filters
     * @throws Exception if an error occurs during security configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable); // disable csrf

        http.authorizeHttpRequests(
                requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(
                                "/v3/api-docs" , "/configuration/ui" , "/swagger-resources/**" ,
                                "/configuration/security" , "/swagger-ui/**" , "/swagger-ui.html" , "/webjars/**" ,
                                "/actuator/**" , "/api/v1/temperatureRecord/**").permitAll()
                        .anyRequest()
                        .authenticated());


        return http.build();

    }

}