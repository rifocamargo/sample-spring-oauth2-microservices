package br.com.lecom.serviceb.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.lecom.serviceb.model.Account;

@FeignClient(name = "service-a")
public interface AccountClient {

	@GetMapping("/service-a/api/accounts")
	List<Account> findAccounts();
	
}
