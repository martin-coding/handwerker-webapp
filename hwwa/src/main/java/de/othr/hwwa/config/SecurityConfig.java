package de.othr.hwwa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
        );

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/webjars/**", "/css/**", "/js/**").permitAll()        // <-- wichtig für CSS/JS
                .requestMatchers("/h2-console/**", "/login", "/logout", "/registration").permitAll()
                .requestMatchers("/", "/home").hasAuthority("basic")
                .requestMatchers("/tasks").hasAuthority("tasks")
                .requestMatchers("/employee/**").hasAuthority("manage_employees")
                .requestMatchers("/profile/company/edit/**").hasAuthority("updateCompanyData")
                .requestMatchers("/clients/**").hasAuthority("manage_clients")
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // redirect after logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}