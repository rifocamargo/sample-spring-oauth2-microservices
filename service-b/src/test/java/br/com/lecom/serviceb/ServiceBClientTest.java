package br.com.lecom.serviceb;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import br.com.lecom.serviceb.model.Customer;

public class ServiceBClientTest {

//	@Test
	public void testClient() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("piomin");
        resourceDetails.setPassword("piot123");
        resourceDetails.setAccessTokenUri("http://localhost:8084/oauth/token");
        resourceDetails.setClientId("service-b");
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read"));
        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
        final Customer customer = restTemplate.getForObject("http://localhost:8083/{id}", Customer.class, 1);
        System.out.println(customer);
	}
	
}
