package com.ey.config;

import com.ey.security.JwtAuthFilter;
import com.ey.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Expose JwtAuthFilter as a bean so it can be injected into the filter chain.
     */
    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthFilter(jwtTokenProvider);
    }

    /**
     * 401 handler: return JSON instead of default HTML.
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, ex) -> {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("""
                {"status":401,"error":"Unauthorized","message":"Authentication required","path":"%s"}
                """.formatted(request.getRequestURI()));
        };
    }

    /**
     * 403 handler: return JSON instead of default HTML.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(403);
            response.setContentType("application/json");
            response.getWriter().write("""
                {"status":403,"error":"Forbidden","message":"Access is denied","path":"%s"}
                """.formatted(request.getRequestURI()));
        };
    }

    /**
     * CORS configuration for local SPA dev. Adjust origins for your UI hosts.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
            "http://localhost:3000", // React
            "http://localhost:4200"  // Angular
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(false); // set true only if you use cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    /**
     * Main security filter chain. Stateless (JWT), CORS enabled, and coarse role gates.
     * Finer authorization is enforced by @PreAuthorize and service-layer ownership checks.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter,
            AuthenticationEntryPoint unauthorizedHandler,
            AccessDeniedHandler accessDeniedHandler,
            UrlBasedCorsConfigurationSource corsSource
    ) throws Exception {

        http
            // CORS & CSRF
            .cors(cors -> cors.configurationSource(corsSource))
            .csrf(csrf -> csrf.disable())

            // Stateless sessions (JWT only)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorization
            .authorizeHttpRequests(auth -> auth
                // Allow CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public auth
                .requestMatchers("/auth/login").permitAll()
                // .requestMatchers("/auth/refresh", "/auth/logout").permitAll() // uncomment if you add these

                // Coarse role gates (fine-grained via @PreAuthorize + service ownership checks)
                .requestMatchers("/doctors/**").hasAnyRole("ADMIN","DOCTOR")
                .requestMatchers("/patients/**").hasAnyRole("ADMIN","PATIENT")
                .requestMatchers("/appointments/**").hasAnyRole("ADMIN","DOCTOR","PATIENT")
                .requestMatchers("/prescriptions/**").hasAnyRole("ADMIN","DOCTOR","PATIENT")

                // (Optional) if you use actuator or swagger, permit these:
                // .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                // .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // Exception handling → JSON 401/403
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler)
            )

            // JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}