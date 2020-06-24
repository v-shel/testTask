package com.ukrpost.test.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ukrpost.test.dao.entity.Payment;
import com.ukrpost.test.enums.PaymentStatus;
import com.ukrpost.test.services.PaymentService;

@Controller
@RequestMapping("/payments")
public class PaymentResource {
	
	@Autowired
	PaymentService payService;
	
	@GetMapping
	@ResponseBody
	public List<Payment> getAllPayments(@RequestParam(value = "userId", required = true) int userId) {
		return payService.findByUserId(userId);
	}
	
	@GetMapping("/with-status")
	@ResponseBody
	public List<Payment> getAllPaidPayments(@RequestParam(value = "payStatus", required = true) PaymentStatus payStatus, 
			@RequestParam(value = "userId", required = true) int userId) {
		
		return payService.findByPayStatus(payStatus.toString(), userId);
	}
}
