package com.dev.mainbackend.security;

import com.dev.mainbackend.security.jwt.AuthEntryPointJwt;
import com.dev.mainbackend.security.jwt.AuthTokenFilter;
import com.dev.mainbackend.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers("/api/{locales}/item/all").permitAll()
				.antMatchers("/api/{locales}/item/{id}").permitAll()
				.antMatchers("/api/{locales}/item/images/{id}").permitAll()
				.antMatchers("/api/{locales}/analyzedText/all").permitAll()
				.antMatchers("/api/{locales}/analyzedText/{id}").permitAll()
				.antMatchers("/api/{locales}/string/all").permitAll()
				.antMatchers("/api/{locales}/string/{id}").permitAll()
				.antMatchers("/api/{locales}/string/images/{id}").permitAll()
				.antMatchers("/api/{locales}/symbol/all").permitAll()
				.antMatchers("/api/{locales}/symbol/{id}").permitAll()
				.antMatchers("/api/{locales}/symbol/images/{id}").permitAll()
				.antMatchers("/api/{locales}/word/all").permitAll()
				.antMatchers("/api/{locales}/word/{id}").permitAll()
				.anyRequest()
				.authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
		return converter;
	}

}
