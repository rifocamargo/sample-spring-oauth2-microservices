package tenant.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import tenant.TenantContext;

public class TenantFilter extends GenericFilterBean {

	private static final String ORIGIN = "origin";
	private static final String X_FORWARDED_HOST = "x-forwarded-host";
	private static final String CLIENT_HOST = "client-host";

	public static final String CLIENT_ID = "client-id";

	private static final String[] BY_PASS = { "/**.png", "/**.jpg", "/**.jpeg", "/**.ico", "/**.js", "/**.css",
			"/**.html" };

	private static final RequestMatcher BY_PASS_REQUEST_MATCHER = new OrRequestMatcher(
			Arrays.asList(BY_PASS).stream().map(AntPathRequestMatcher::new).collect(Collectors.toList()));

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest) request;

		if (requiresByPass(httprequest)) {
			chain.doFilter(request, response);
			return;
		}

		System.out.println("In filter we are Intercepting the Request");
		System.out.println("---------------------------------------------");
		this.setTenant(httprequest);
		System.out.println("RequestURI: " + httprequest.getRequestURI());
		System.out.println("---------------------------------------------");
		chain.doFilter(request, response);
	}

	private void setTenant(HttpServletRequest httpServletRequest) {
		Optional<String> optionalClientHost = Optional.ofNullable(httpServletRequest.getHeader(CLIENT_HOST));
		Optional<String> optionalClientId = Optional.ofNullable(httpServletRequest.getHeader(CLIENT_ID));
		Optional<String> optionalOrigin = Optional.ofNullable(httpServletRequest.getHeader(ORIGIN));
		Optional<String> optionalXForwardedHost = Optional.ofNullable(httpServletRequest.getHeader(X_FORWARDED_HOST));

		String tenantID = optionalClientId.or(() -> optionalClientHost).or(() -> optionalOrigin)
				.or(() -> optionalXForwardedHost).orElse(httpServletRequest.getServerName());

		System.out.println("Search for tenant  :: " + tenantID);

		TenantContext.setCurrentTenant(tenantID);

	}

	protected boolean requiresByPass(final HttpServletRequest request) {
		return BY_PASS_REQUEST_MATCHER.matches(request);
	}
}