package br.com.lecom.servicea.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import tenant.filter.TenantFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.addFilterBefore(new TenantFilter(), WebAsyncManagerIntegrationFilter.class)
			.securityContext().securityContextRepository(this.httpSessionSecurityContextRepository()).and()
				.authorizeRequests()
		        	.antMatchers("/login**").permitAll()
		        	.anyRequest().authenticated()
		        .and()
		        	.oauth2Login()
		        		.loginPage("/oauth2/authorization/oauth-service")
		        .and()
		        	.logout()
		        		.logoutSuccessUrl("https://localhost:8084/logout");
 		// @formatter:on
	}
	
	@Bean
	public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}

}