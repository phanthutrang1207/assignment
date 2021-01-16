package com.nab.trang.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApiGatewayConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
        // allow anonymous access to the root page
		.antMatchers("/actuator").permitAll()
        .antMatchers("/actuator/**").permitAll()
        // all other requests
        .anyRequest().authenticated()
        .and()
        .oauth2ResourceServer().jwt(); // replace .jwt() with .opaqueToken() for Opaque Token case

	}
}
