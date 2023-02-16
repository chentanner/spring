package com.sss.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ApiSecurityChainConfiguration {

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests.antMatchers("/**").permitAll()
//                                .anyRequest().authenticated())
//                .csrf().disable();

        applyPublicHttpBasic(http);

        applyAuthenticatedHttp(http);
        return http.build();
    }

    private static void applyPublicHttpBasic(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                                           .antMatchers("/public/**").permitAll()
//                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
    }

//    @Bean
//    public SecurityFilterChain authenticatedFilterChain(HttpSecurity http) throws Exception {
//
//        return getAuthenticatedHttp(http);
//    }

    private static void applyAuthenticatedHttp(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auths) -> auths
                                           .antMatchers("/**")
                                           .authenticated()
//                                           .hasAuthority("SCOPE_message:read")//.permitAll()
//                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Bean
    public WebSecurityCustomizer webSecurityIgnoringCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }

}