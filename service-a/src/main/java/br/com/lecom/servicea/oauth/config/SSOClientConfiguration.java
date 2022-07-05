package br.com.lecom.servicea.oauth.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import feign.Logger;
import feign.RequestInterceptor;
import tenant.TenantContext;
import tenant.filter.TenantFilter;

@Configuration
public class SSOClientConfiguration {
	
	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(ClientCredentialsResourceDetails oauth2RemoteResource) {
		OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor = new OAuth2FeignRequestInterceptor(
				new DefaultOAuth2ClientContext(), oauth2RemoteResource);
		ClientCredentialsAccessTokenProvider clientCredentialsAccessTokenProvider = new ClientCredentialsAccessTokenProvider();
		clientCredentialsAccessTokenProvider.setInterceptors(Arrays.asList(new RestTemplateHeaderModifierInterceptor()));
		oAuth2FeignRequestInterceptor.setAccessTokenProvider(clientCredentialsAccessTokenProvider);
		return oAuth2FeignRequestInterceptor;
	}

	class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
			request.getHeaders().add(TenantFilter.CLIENT_ID, TenantContext.getCurrentTenant());
			return execution.execute(request, body);
		}
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
