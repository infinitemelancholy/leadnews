package com.leadnews.app.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //鍏佽鎵€鏈夌殑鏂规硶
        config.addAllowedMethod("*");
        //杩愯鎵€鏈夌殑鍩熻繘琛岃姹?
        config.addAllowedOrigin("*");
        //鍏佽鎵€鏈夌殑璇锋眰澶?
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);//閽堝鎵€鏈夌殑璇锋眰閮芥敮鎸佽法鍩?
        return new CorsWebFilter(source);
    }
}
