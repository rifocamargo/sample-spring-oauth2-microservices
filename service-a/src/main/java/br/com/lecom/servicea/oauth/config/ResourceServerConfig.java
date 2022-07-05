package br.com.lecom.servicea.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import tenant.filter.TenantFilter;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private ResourceServerProperties resource;

	@Autowired
	private HttpSessionSecurityContextRepository httpSessionSecurityContextRepository;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.addFilterBefore(new TenantFilter(), WebAsyncManagerIntegrationFilter.class)
			.securityContext().securityContextRepository(httpSessionSecurityContextRepository)
			.and()
			.requestMatchers()
				.antMatchers("/api/**")
			.and()
				.authorizeRequests()
					.anyRequest().authenticated();
		// @formatter:on
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(this.tokenServices(resource)).stateless(false);
	}

	@Bean
	@Primary
	public ResourceServerTokenServices tokenServices(ResourceServerProperties resource) {
		MyRemoteTokenServices services = new MyRemoteTokenServices();
		services.setCheckTokenEndpointUrl(resource.getTokenInfoUri());
		services.setClientId(resource.getClientId());
		services.setClientSecret(resource.getClientSecret());
		return services;
	}

}
