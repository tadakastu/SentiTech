import java.io.IOException;
import java.sql.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;



public class UserAuthentication extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationmanager;
	
	
	//Constructor for our UserAuthentication class.
	public UserAuthentication(AuthenticationManager authenticationmanager) {
		this.authenticationmanager = authenticationmanager;
		
		setFilterProcessesUrl("/api/services/controller/user/login");
		
	}
	
	//Attempt Authentication runs when a login attempt to is made. Credentials are read and checked in order to authenticate.
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			User creds = new ObjectMapper().readValue(request.getInputStream(), User.class);
			
			return authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(
													 creds.getUsername(),
													 creds.getPassword()));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	//If authentication is successful then the following is called. Returns a token that defines the expiration time and the secret key.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
											FilterChain chain, Authentication authenticate) throws IOException {
		String token = JWT.create().withSubject(((User) authenticate.getPrincipal()).getUsername())
								   .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
								   .sign(Algorithm.HMAC512(SECRET.getBytes()));
		
		String body = ((User) authenticate.getPrincipal()).getUsername() + " " + token;
		
		response.getWriter().write(body);
		response.getWriter().flush();
		
	}
	
}
