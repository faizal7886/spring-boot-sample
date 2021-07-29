package com.example.demo.controller;


import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.emailService;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService  service;
	
	@Autowired
	private emailService email;
	
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

	
	@GetMapping("/")
	public String Basedata() {
		log.info("Application running successfully");
		return "service is up and running";
	}
	
	 @GetMapping(value = "/sendemail/{id}")
	   public String sendEmail(@PathVariable Integer id) throws AddressException, MessagingException, IOException {
		
	      return "Email sent successfully to .........."+ email.sendEmail(id) ;
	   } 
	
	@GetMapping("/getEmployee")
	public List<Employee> Employeesdata() {
		log.error("consider this as error ");
		return service.getData();
	}
	
	
	  @PostMapping("/upload")
	  public String uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";

	    
	      try {
	        email.save(file);

	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
	        return message;
	      } catch (Exception e) {
	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	        return message;
	      }
	    

	    
	   
	  }
	
	  
	  
	  @GetMapping("/download")
	  public ResponseEntity<Resource> getFile() {
	    String filename = "tutorials.xlsx";
	    InputStreamResource file = new InputStreamResource(email.load());

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }
	
	
	

}

