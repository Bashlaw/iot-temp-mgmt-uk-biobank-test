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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(
                                "/v3/api-docs" , "/configuration/ui" , "/swagger-resources/**" ,
                                "/configuration/security" , "/swagger-ui/**" , "/swagger-ui.html" , "/webjars/**" ,
                                "/actuator/**", "/api/v1/temperatureRecord/**").permitAll()
                        .anyRequest()
                        .authenticated());


        return http.build();
    }

}