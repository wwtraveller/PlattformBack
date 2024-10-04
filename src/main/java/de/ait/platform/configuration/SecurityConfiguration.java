package de.ait.platform.configuration;

import de.ait.platform.security.service.TokenFilter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenFilter filter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        x -> x
                                 .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/login", "/api/refresh").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/check-username").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/changePassword").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.GET, "/api/check-email").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/articles").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/auth/me").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/articles/{id}").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.POST, "/api/articles").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/articles/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/articles/{id}").hasRole("ADMIN")
                                //.requestMatchers(HttpMethod.GET, "/api/categories/articles").permitAll()                  //new
                                .requestMatchers(HttpMethod.GET, "/api/categories/findarticles").permitAll()  //new
                                .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/articles/{articleId}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/categories/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.GET, "/api/comments").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/comments/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.PUT, "/api/comments/{id}").hasAnyRole("ADMIN","USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/comments/{id}").hasAnyRole("ADMIN","USER")
                                .anyRequest().authenticated()

                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "https://platform-qxs32.ondigitalocean.app", "https://platform-qxs32.ondigitalocean.app/api/swagger-ui/index.html", "https://platform-qxs32.ondigitalocean.app/api/swagger-ui/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
