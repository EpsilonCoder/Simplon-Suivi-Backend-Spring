package com.simplonsuivi.co.resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.simplonsuivi.co.constant.FileConstant;
import com.simplonsuivi.co.constant.SecurityConstant;
import com.simplonsuivi.co.domain.HttpResponse;
import com.simplonsuivi.co.domain.User;
import com.simplonsuivi.co.domain.UserPrincipal;
import com.simplonsuivi.co.exception.domain.EmailExistException;
import com.simplonsuivi.co.exception.domain.EmailNotFoundException;
import com.simplonsuivi.co.exception.domain.ExceptionHandling;
import com.simplonsuivi.co.exception.domain.UserNotFoundException;
import com.simplonsuivi.co.service.UserService;
import com.simplonsuivi.co.utility.JWTTokenProvider;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin("http://localhost:4200")
public class UserResource extends ExceptionHandling {
	
	private UserService userService;
	
	private AuthenticationManager authenticationManager;
	
	private JWTTokenProvider jwtTokenProvider;
	
	@Autowired
	public UserResource(UserService userService, AuthenticationManager authenticationManager,
			JWTTokenProvider jwtTokenProvider) {
		super();
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")

    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }
	
	
	 @PostMapping("/add")
	    public ResponseEntity<User> addNewUser(@RequestParam String firstName,
	                                           @RequestParam String lastName,
	                                           @RequestParam String username,
	                                           @RequestParam String email,
	                                           @RequestParam String role,
	                                           @RequestParam String isActive,
	                                           @RequestParam String isNotLocked,
	                                           @RequestParam(required = false) MultipartFile profileImage)
	            throws UserNotFoundException,com.simplonsuivi.co.exception.domain.UsernameExistException, EmailExistException, Exception {

	        User newUser = this.userService.addNewUser(firstName, lastName, username, email, role,
	                Boolean.parseBoolean(isNotLocked), Boolean.parseBoolean(isActive), profileImage);
	        return new ResponseEntity<>(newUser, HttpStatus.OK);
	    }
	
	
	@PostMapping("update")
	public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUserName,
			                                 @RequestParam("firstName") String firstName,
			                                 @RequestParam("lastName") String lastName,
			                                 @RequestParam("username") String username,
			                                 @RequestParam("email") String email,
			                                 @RequestParam("role") String role,
			                                 @RequestParam("isActive") String isActive,
			                                 @RequestParam("isNotLocked") String isNotLocked,
			                                 @RequestParam(value = "profileImage",required = false) MultipartFile profileImage) throws UserNotFoundException,com.simplonsuivi.co.exception.domain.UsernameExistException, EmailExistException, Exception{
		
	 User updatedUser=userService.updateUser(currentUserName,firstName, lastName, username, email, role, Boolean.parseBoolean(isNotLocked), Boolean.parseBoolean(isActive), profileImage);
												return new ResponseEntity<> (updatedUser,OK);
		
	}
	
	@GetMapping("/find/{username}")
	public ResponseEntity<User> getUser(@PathVariable("username") String username){
		User user=userService.findUserByUsername(username);
		return new ResponseEntity<> (user,OK);
	}
	
	
	@GetMapping("/list")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users=userService.getUser();
		return new ResponseEntity<> (users,OK);
	}
		
	
	 @GetMapping("/reset-password/{email}")
	    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String email)
	            throws EmailNotFoundException, MessagingException {
	        this.userService.resetPassword(email);
	        return this.response(HttpStatus.OK,SecurityConstant.EMAIL_SENT.concat(email));
	    }
	 
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(), httpStatus);
    }

	//@PreAuthorize, cette annotation est possible car nous avons configuré @EnableGlobalMethodSecurity(prePostEnabled = true)
    @DeleteMapping("/delete/{username}")
    //@PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable String username)
            throws UserNotFoundException, IOException {
        this.userService.deleteUser(username);
        return this.response(HttpStatus.OK, "L'utilisateur a bien ete supprimé");
    }

	@PostMapping("updateProfileImage")
	public ResponseEntity<User> updateProfilImage(
			                                 @RequestParam("username") String username,
			                                 @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException,com.simplonsuivi.co.exception.domain.UsernameExistException, EmailExistException, Exception{
		User user=userService.updateProfileImage(username, profileImage);
	    return new ResponseEntity<> (user,OK);
		
	}
	
	   @GetMapping(path = "/image/{username}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
	        return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + fileName));
	    }

	    @GetMapping(path = "/image/profile/{username}", produces =MediaType.IMAGE_JPEG_VALUE)
	    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
	        URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        try (InputStream inputStream = url.openStream()) {
	            int bytesRead;
	            byte[] chunk = new byte[1024];
	            while((bytesRead = inputStream.read(chunk)) > 0) {
	                byteArrayOutputStream.write(chunk, 0, bytesRead);
	            }
	        }
	        return byteArrayOutputStream.toByteArray();
	    }
	
	
	
	

	private HttpHeaders getJwtHeader(UserPrincipal user) {
		HttpHeaders headers=new HttpHeaders();
		headers.add(SecurityConstant.JWT_TOKEN_HEARDER, jwtTokenProvider.generateJwtToken(user));
		return headers;
	}
	private void authenticate(String username, String password) {
	  
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		
	}

	@PostMapping("/register")
	 public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, MessagingException, com.simplonsuivi.co.exception.domain.UsernameExistException{		
	 User newUser=	userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
	 return new ResponseEntity<>(newUser,OK);
	}

}
