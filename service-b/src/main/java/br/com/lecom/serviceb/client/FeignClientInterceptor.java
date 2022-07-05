package br.com.lecom.serviceb.client;

import org.springframework.context.annotation.Configuration;

import br.com.lecom.serviceb.tenant.TenantContext;
import br.com.lecom.serviceb.tenant.filter.TenantFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {
	
	@Override
	public void apply(RequestTemplate template) {
		
		template.header(TenantFilter.CLIENT_ID, TenantContext.getCurrentTenant());
	}
}