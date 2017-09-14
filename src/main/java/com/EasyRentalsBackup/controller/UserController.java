package com.EasyRentalsBackup.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EasyRentalsBackup.model.User;
import com.EasyRentalsBackup.repository.UserRepository;
import com.EasyRentalsBackup.service.SignupConformationMail;




@RestController
@EnableAutoConfiguration
@RequestMapping(value="/EasyRentals")
public class UserController {

	String jwtToken = "";
	@Autowired
	SignupConformationMail signupConformationmail;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = "/registerUser",method=RequestMethod.POST)
	public String registerUser(@RequestBody User user)
	{
		logger.info("First name in register user-->"+user.getfName());
		logger.info("Last name in register user-->"+user.getlName());
		
		List<User> users= userRepository.findAll();
		user.setUserId(users.size()+1);
		userRepository.save(user);
		
		
			try
			{
			signupConformationmail.sendNotification(user);
			//logger.info("Success");
			}
			catch(MailException e)
			{
				//logger.info("Error Sending Email     "+e.getMessage());
			}
        return "{\"value\":\"Thanks for Signup\" }";
	}
	   @RequestMapping(value = "/hello", method = RequestMethod.GET)
	   public ResponseEntity<String> get() {
	       return new ResponseEntity<String>("Hello World", HttpStatus.OK);
	   }
	   @RequestMapping(value = "/getUserDetails", method= RequestMethod.POST, produces = "application/json")
	    public String validateUser(@RequestBody User userDtls) {
		
		   logger.info("In validateUserr-->");
			logger.info("Email Address-->"+userDtls.getEmail());
			logger.info("Password-->"+userDtls.getPassword());
			
			 if (userDtls.getEmail() == null || userDtls.getPassword() == null) {
			        return "{\"value\":\"Please fill in username and password\"}";
			    }

			User u1=userRepository.findByEmailAndPassword(userDtls.getEmail(),userDtls.getPassword());
			
			if(u1==null){
				logger.info("Invalid Credential-->");
				return "{\"value\":\"false\"}";
				
			}
			else{
				logger.info("Logged in Successfully-->");
				
				logger.info(u1.getfName());
				//return "{\"value\":\""+u1.getfName()+" "+u1.getlName()+"\"}";
				return "{\"value\":\"true\",\n \"fName:\": \"u1.getfName u1.getlName()+\"}";
				
				 
			}
	   }

	   @RequestMapping(value="/deleteUser", method=RequestMethod.DELETE)
	   public String deleteUser(@RequestParam(value="id") Long ID)
	   {
		   userRepository.delete(ID);
		   return "Successfully deleted";
	   }
	   
	   @RequestMapping(value="/updateUser", method=RequestMethod.PUT)
	   public String updateUser(@RequestParam(value="id")Long ID, @RequestBody User user)
	   {
		   
		   List<User> u= userRepository.findById(ID);
			if(u==null)
			{
				System.out.println("Sorry"+ID+"not Found");
				
			}
			user.setfName(user.getfName());
			user.setlName(user.getlName());
			user.setEmail(user.getEmail());
			user.setPassword(user.getPassword());
			return "Successfully Updated";
	   }

}
			