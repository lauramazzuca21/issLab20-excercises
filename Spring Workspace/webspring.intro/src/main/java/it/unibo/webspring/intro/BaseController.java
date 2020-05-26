package it.unibo.webspring.intro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class BaseController { 
//    @Value("${spring.application.name}")
//    String appName;
//
//  @GetMapping("/") //significato sul LabSpringIntro 		 
//  public String homePage(Model viewModel) { //viewmodel is NOT the application model
//	  System.out.println("------------------- BaseController homePage " + viewModel  );
//	  viewModel.addAttribute("arg", appName);
//	  return "welcome"; //in fact, this is the welcome.html
//   } 
//    
//    @ExceptionHandler 
//    public ResponseEntity handle(Exception ex) {
//    	HttpHeaders responseHeaders = new HttpHeaders();
//        return new ResponseEntity(
//        		"BaseController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
//    }

}