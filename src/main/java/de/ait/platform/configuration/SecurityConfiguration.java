package de.ait.platform.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        x -> x
                                .requestMatchers(HttpMethod.GET, "/api/articles").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/articles/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/articles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/articles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/articles/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/categories").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/comments").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/comments/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/comments").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/comments").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/comments/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());


        return http.build();
    }
}
