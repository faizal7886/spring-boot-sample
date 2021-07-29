package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Employee;
import com.example.demo.repository.employeeRepository;

@Service
public class emailService {
	
	
	
	private static final Logger log = LoggerFactory.getLogger(emailService.class);
	
	static String SHEET = "Sheet1";

	 @Autowired
	    private JavaMailSender javaMailSender;
	 
	 @Autowired
	 private employeeRepository repo;
	    
	   public  String sendEmail(Integer id) {

	        SimpleMailMessage msg = new SimpleMailMessage();
	        Employee emp=repo.findById(id).get();
	        System.out.println(emp.getEmail());
	        msg.setTo(emp.getEmail());

	        msg.setSubject("Testing from Spring Boot based on id");
	        msg.setText("Hello World \n Spring Boot Email");
            log.warn("email is working properly");
	        javaMailSender.send(msg);
	        return emp.getEmail();

	    }
	   
	   
	   public void save(MultipartFile file) {
		    try {
		      List<Employee> tutorials = excelToTutorials(file.getInputStream());
		      repo.saveAll(tutorials);
		    } catch (IOException e) {
		      throw new RuntimeException("fail to store excel data: " + e.getMessage());
		    }
		  }
	   
	   
	   
	   public static List<Employee> excelToTutorials(InputStream is) {
		    try {
		      Workbook workbook = new XSSFWorkbook(is);

		      Sheet sheet = workbook.getSheet(SHEET);
		      Iterator<Row> rows = sheet.iterator();

		      List<Employee> tutorials = new ArrayList<Employee>();

		      int rowNumber = 0;
		      while (rows.hasNext()) {
		        Row currentRow = rows.next();

		        // skip header
		        if (rowNumber == 0) {
		          rowNumber++;
		          continue;
		        }

		        Iterator<Cell> cellsInRow = currentRow.iterator();

		        Employee tutorial = new Employee();

		        int cellIdx = 0;
		        while (cellsInRow.hasNext()) {
		          Cell currentCell = cellsInRow.next();

		          switch (cellIdx) {
		          case 0:
		            tutorial.setId((int)currentCell.getNumericCellValue());
		            break;

		          case 1:
		            tutorial.setName(currentCell.getStringCellValue());
		            break;

		          case 2:
		            tutorial.setEmail(currentCell.getStringCellValue());
		            break;

		          
		          default:
		            break;
		          }

		          cellIdx++;
		        }

		        tutorials.add(tutorial);
		      }

		      workbook.close();

		      return tutorials;
		    } catch (IOException e) {
		      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		    }
		  }
	   
	   
	   static String SHEET1 = "Tutorials";
	   static String[] HEADERs = { "Id", "Name", "Email"};
	   
	   
	   public ByteArrayInputStream load() {
		    List<Employee> tutorials = repo.findAll();

		    ByteArrayInputStream in =tutorialsToExcel(tutorials);
		    return in;
		  }

	   public static ByteArrayInputStream tutorialsToExcel(List<Employee> tutorials) {

	     try (Workbook workbook = new XSSFWorkbook();
	    		 ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	       Sheet sheet = workbook.createSheet(SHEET1);

	       // Header
	       Row headerRow = sheet.createRow(0);

	       for (int col = 0; col < HEADERs.length; col++) {
	         Cell cell = headerRow.createCell(col);
	         cell.setCellValue(HEADERs[col]);
	       }

	       int rowIdx = 1;
	       for (Employee tutorial : tutorials) {
	         Row row = sheet.createRow(rowIdx++);

	         row.createCell(0).setCellValue(tutorial.getId());
	         row.createCell(1).setCellValue(tutorial.getName());
	         row.createCell(2).setCellValue(tutorial.getEmail());
	        // row.createCell(3).setCellValue(tutorial.isPublished());
	       }

	       workbook.write(out);
	       return new ByteArrayInputStream(out.toByteArray());
	     } catch (IOException e) {
	       throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	     }
	   }
	   
	   
		
	   
}
