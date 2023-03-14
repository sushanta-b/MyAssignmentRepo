package com.thinktalentindia.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thinktalentindia.demo.helper.DemoHelper;

/**
 * @author sushanta This is responsible to drive the user related queries
 */
@RestController
@RequestMapping("/demo")
public class FrontController {

	private DemoHelper demoHelper = new DemoHelper();
	/*
	 * Used to handle default get request.
	 */
	@RequestMapping("/")
	String fallBack() {
		return "Hello from fallBack";
	}

	/*
	 * This method is responsible to get Name from given parameters by using
	 * specified business logic.
	 */
	@GetMapping("/name")
	public String getName(@RequestParam String firstName, @RequestParam(required = false) String middleName,
			@RequestParam String lastName) {

	    if(!demoHelper.isValidString(firstName) || !demoHelper.isValidString(lastName))
	    	return "Invalid request: firstName or lastName shoud not be empty...";
	    
		StringBuilder formattedName = new StringBuilder();		
		/* Add the first letter of the first name */
		formattedName.append(Character.toUpperCase(firstName.charAt(0))).append(" ");

		/* Add the first letter of the middle name, if it exists */
		if (middleName != null && !middleName.isEmpty()) {
			formattedName.append(Character.toUpperCase(middleName.charAt(0))).append(" ");
		}
		/* Add the last name in all caps */
		formattedName.append(lastName.toUpperCase());
		return formattedName.toString();
	}
}