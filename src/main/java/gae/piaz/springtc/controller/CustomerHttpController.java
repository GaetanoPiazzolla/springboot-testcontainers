package gae.piaz.springtc.controller;

import gae.piaz.springtc.data.Customer;
import gae.piaz.springtc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class CustomerHttpController {

	private final CustomerService customerService;

	@GetMapping(value = "/customers", produces = "application/json")
	public List<Customer> customers() {
		List<Customer> customers = this.customerService.findAll();
		log.info("Found {} customers",customers.size());
		return customers;
	}

	@GetMapping("/customers/{name}")
	public List<Customer> byName(@PathVariable String name) {
		List<Customer> customers = this.customerService.findByName(name);
		log.info("Found {} customers",customers.size());
		return customers;
	}

	@PostMapping(value = "/customers", consumes = "application/json")
	public void saveCustomer(@RequestBody CustomerData customerData) {
		customerService.saveAsync(customerData);
		log.info("Saved asynchronously a new customer");
	}

}