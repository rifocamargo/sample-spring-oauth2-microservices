package br.com.lecom.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JdbcTokenStore tokenStore;
	
	@Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.expressionHandler(new DefaultWebSecurityExpressionHandler()).authenticationManager(this.authenticationManager).tokenStore(this.tokenStore);
    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//    	// @formatter:off
//	   	 http
////	    	.requestMatchers()
////	        	.antMatchers("/oauth/**")
////	        	.and()
//			.authorizeRequests()
//				.anyRequest().authenticated()
//				.and()
//			.formLogin()
//				.permitAll()
//				.and()
//			.logout()
//				.permitAll();
//		// @formatter:on
//    }
}
