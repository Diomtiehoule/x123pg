package com.df.fne.presenter.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***********************************************************************
 * @Projet: com.df.paiementlot.api
 * @Author: MORY SANG.
 * @Description: Class WebConfig created on 17/07/2025
 ***********************************************************************/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Utilisez allowedOriginPatterns au lieu de allowedOrigins
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
