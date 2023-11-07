package com.example.morax.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val logoutService: LogoutHandler,
    private val authEntryPoint: AuthEntryPoint
) {
    private val whiteListURL = arrayOf(
        "api/v1/user/login",
        "/api/v1/user/register",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html",
        "/error"
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { obj -> obj.disable() }
            .authorizeHttpRequests { req ->
                req.requestMatchers(*whiteListURL).permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .exceptionHandling {exception  -> exception.authenticationEntryPoint(authEntryPoint)}
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement { STATELESS }
            .logout { logout ->
                logout.logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutService)
                    .logoutSuccessHandler{ _, _, _ -> SecurityContextHolder.clearContext()}
            }

        return http.build()
    }
}
