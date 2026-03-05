package com.sanjay.PandasClothing;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.sanjay.PandasClothing.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/shirts.html",
                                "/cargo.html",
                                "/Tshirts.html",
                                "/shoes.html",
                                "/account.html",
                                "/register.html",
                                "/about.html",
                                "/Contact Us.html",
                                "/privacy & policy.html",
                                "/Terms & Conditions.html",
                                "/verification.html",
                                "/api/users/register",
                                "/api/users/verify",
                                "/css/**",
                                "/images/**",
                                "/js/**",
                                "/panda.png",
                                "/register",
                                "/login",
                                "/current-user"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/account.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html", true)  // ✅ redirect after login
                        .failureUrl("/account.html?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                                }
                        )
                );

        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}