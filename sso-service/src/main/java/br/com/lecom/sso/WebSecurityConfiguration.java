package br.com.lecom.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.requestMatchers()
				.antMatchers("/login**", "/oauth/**")
			.and()
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
//				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll()
//			.and()
//			.addFilterBefore(this.resourcesServerFilter(), AbstractPreAuthenticatedProcessingFilter.class)
//			.exceptionHandling()
//			.defaultAuthenticationEntryPointFor(auth2AuthenticationEntryPoint(), new AntPathRequestMatcher("/api/**"))
				;
//		 http.requestMatchers()
//	         .antMatchers("/login", "/oauth/authorize", "/oauth/token")
//	         .and()
//	         .authorizeRequests().antMatchers("/login", "/oauth/authorize", "/oauth/token").permitAll()
//	         .anyRequest().authenticated()
//	         .and()
//	         .formLogin().permitAll();
		// @formatter:on

	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("SHA-256");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth
        	.userDetailsService(userDetailsService)
        	.passwordEncoder(passwordEncoder());
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() 
      throws Exception {
        return super.authenticationManagerBean();
    }
	
//    @Bean
//	public OAuth2AuthenticationEntryPoint auth2AuthenticationEntryPoint() {
//		return new OAuth2AuthenticationEntryPoint();
//	}
//	
//	@Bean
//	public OAuth2AuthenticationProcessingFilter resourcesServerFilter() throws Exception {
//		AuthenticationManager oauthAuthenticationManager = authenticationManagerBean();
//		OAuth2AuthenticationProcessingFilter resourcesServerFilter = new OAuth2AuthenticationProcessingFilter();
//		resourcesServerFilter.setAuthenticationEntryPoint(auth2AuthenticationEntryPoint());
//		resourcesServerFilter.setAuthenticationManager(oauthAuthenticationManager);
//		resourcesServerFilter.setStateless(false);
//		return resourcesServerFilter;
//	}

}
