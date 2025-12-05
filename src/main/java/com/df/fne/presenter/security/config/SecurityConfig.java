package com.df.fne.presenter.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthentificationFilter jwtAuthentificationFilter;

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                //.cors(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // Désactive la redirection automatique vers /login
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                        })
                        // Gère les accès refusés
                        .accessDeniedHandler((req, res, ex) -> {
                            res.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");
                        })
                )

                        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                /*Chaque requête doit être entièrement autonome (stateless), c’est-à-dire que toutes les informations d'authentification
                 doivent être envoyées à chaque requête (souvent via un token JWT*/
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /*sert à désactiver la protection CSRF (Cross-Site Request Forgery).*/
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(ar -> ar.requestMatchers("/api/auth/**").permitAll())
                .authorizeHttpRequests(ar -> ar.requestMatchers(SWAGGER_WHITELIST).permitAll())
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        /*configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
*/

        // ✅ Méthodes HTTP et frontend autorisées
        //configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedOriginPatterns(List.of("*"));  // <-- Changement clé ici
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        //configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        // Headers visibles côté client dans la réponse
        //configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition", "X-Total-Count"));
        // Permet d'envoyer les cookies (ex: tokens)
        // Headers autorisés
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
