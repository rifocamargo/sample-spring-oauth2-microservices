package br.com.lecom.servicea.oauth.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import tenant.TenantContext;
import tenant.filter.TenantFilter;

public class MyRemoteTokenServices implements ResourceServerTokenServices {

	private static final String ACTIVE = "active";

	protected final Log logger = LogFactory.getLog(getClass());

	private RestOperations restTemplate;

	private String checkTokenEndpointUrl;

	private String clientId;

	private String clientSecret;

    private String tokenName = "token";

	private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

	public MyRemoteTokenServices() {
		restTemplate = new RestTemplate();
		((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			// Ignore 400
			public void handleError(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode() != HttpStatus.BAD_REQUEST) {
					super.handleError(response);
				}
			}
		});
	}

	public void setRestTemplate(RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
		this.checkTokenEndpointUrl = checkTokenEndpointUrl;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.tokenConverter = accessTokenConverter;
	}

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
	public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add(tokenName, accessToken);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
		headers.set(TenantFilter.CLIENT_ID, TenantContext.getCurrentTenant());
		headers.set(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(clientId, clientSecret));
		Map<String, Object> map = postForMap(checkTokenEndpointUrl, formData, headers);

		this.checkResponseError(map, accessToken);

		return tokenConverter.extractAuthentication(map);
	}
    
    private void checkResponseError(Map<String, Object> responseMap, String accessToken) {
    	if (responseMap != null) {
    		if (responseMap.containsKey("error")) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("check_token returned error: " + responseMap.get("error"));
    			}
    			throw new InvalidTokenException(accessToken);
    		}
    		
    		// gh-838
    		if (!Boolean.TRUE.equals(responseMap.get(ACTIVE))) {
    			logger.debug("check_token returned active attribute: " + responseMap.get(ACTIVE));
    			throw new InvalidTokenException(accessToken);
    		}
    	}    	
    }

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

	private String getAuthorizationHeader(String clientId, String clientSecret) {

		if(clientId == null || clientSecret == null) {
			logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
		}

		String creds = String.format("%s:%s", clientId, clientSecret);
		return "Basic " + new String(Base64.getEncoder().encode(creds.getBytes(StandardCharsets.UTF_8)));
	}

	private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
		if (headers.getContentType() == null) {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		@SuppressWarnings("rawtypes")
		Map map = restTemplate.exchange(path, HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = map;
		return result;
	}

}
