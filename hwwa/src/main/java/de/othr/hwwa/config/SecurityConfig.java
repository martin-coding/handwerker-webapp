package de.othr.hwwa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationSuccessHandler twoFactorAuthenticationSuccessHandler() {
        return new TwoFactorAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(
            HttpSecurity http,
            JwtFilter jwtFilter
    ) throws Exception {

        http
                .securityMatcher("/api/**")

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
        );

        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/webjars/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/h2-console/**", "/login", "/logout", "/registration").permitAll()
                .requestMatchers("/", "/home").hasAuthority("basic")
                .requestMatchers("/tasks/**").hasAuthority("tasks")
                .requestMatchers("/employee/**").hasAuthority("manageEmployees")
                .requestMatchers("/profile/company/edit/**").hasAuthority("updateCompanyData")
                .requestMatchers("/clients/**").hasAuthority("manageClients")
                .requestMatchers("/invoices/**").hasAuthority("manageInvoices")
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .successHandler(twoFactorAuthenticationSuccessHandler())
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}