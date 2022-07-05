package br.com.lecom.serviceb.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {
	private static final ThreadLocal<String> CURRENT_TENANT = new InheritableThreadLocal<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(TenantContext.class);

	private TenantContext() {
	}

	public static String getCurrentTenant() {
		return CURRENT_TENANT.get();
	}

	public static void setCurrentTenant(String tenant) {
		LOGGER.info("Setting tenant: {}", tenant);
		CURRENT_TENANT.set(tenant);
	}

	public static void clear() {
		LOGGER.info("Cleaning context for tenant: {}", getCurrentTenant());
		CURRENT_TENANT.remove();
	}
}
