package com.Insumos.Servicios.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CrossFilterConfig {

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsCofig = new CorsConfiguration();

        corsCofig.addAllowedOriginPattern("*");

        corsCofig.addAllowedMethod("GET");
        corsCofig.addAllowedMethod("POST");
        corsCofig.addAllowedMethod("PUT");
        corsCofig.addAllowedMethod("DELETE");
        corsCofig.addAllowedMethod("PATCH");

        corsCofig.addAllowedHeader("*");
        corsCofig.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsCofig);

        return new CorsFilter(source);

    }

}
