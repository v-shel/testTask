package com.ukrpost.test.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ukrpost.test.dao.entity.Discount;
import com.ukrpost.test.dao.entity.Product;
import com.ukrpost.test.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductService prodService;
	
	@PostMapping
	@ResponseBody
	public Product create(@NotNull @Valid @RequestBody Product product) {
		return prodService.create(product);
	}
	
	@ResponseBody
	@GetMapping("/id/{prodId}")
	public Product getById(@NotNull @PathVariable(name = "prodId", required = true) int prodId) {
		return prodService.findById(prodId);
	}
	
	@ResponseBody
	@GetMapping("/name/{name}")
	public Iterable<Product> getByName(@NotNull @PathVariable(name = "name", required = true) String name) {
		return prodService.findByName(name);
	}
	
	@ResponseBody
	@GetMapping("/available")
	public Iterable<Product> getAvailable() {
		return prodService.findAvailable();
	}
	
	@ResponseBody
	@GetMapping("/discounted")
	public Iterable<Product> getDiscounted() {
		return prodService.findWithDiscount();
	}
	
	@ResponseBody
	@PostMapping("/{prodId}")
	public Product addDiscount(@PathVariable(name = "prodId", required = true) int prodId, 
			@RequestBody Discount discount) {
		
		return prodService.addDiscount(prodId, discount);
	}
	
	@DeleteMapping("/{prodId}")
	public ResponseEntity<String> addDiscount(@PathVariable(name = "prodId", required = true) int prodId) {
		prodService.deleteById(prodId);
		return ResponseEntity.ok("");
	}
}
