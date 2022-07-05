package br.com.lecom.serviceb.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lecom.serviceb.client.AccountClient;
import br.com.lecom.serviceb.model.Customer;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	AccountClient accountClient;
	
	@GetMapping("/{id}")
	public Customer findCustomer(@PathVariable("id") Integer id) {
		Customer c = new Customer(id, "John Smith", 33);
		LOGGER.info("Customer found: id={}", c.getId());
		c.setAccounts(accountClient.findAccounts());
		LOGGER.info("Accounts found: size={}", c.getAccounts().size());
		return c;
	}
	
}
