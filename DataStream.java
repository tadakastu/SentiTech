import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@PropertySource("classpath:application.properties")
public class DataStream {
	 public void DataStream() {
	    	
	}
	//Set up logging
	private static final Logging log = getLogger(DataStream.class);
	
	//Set up the Rest Server configuration using applicationContext.
	ApplicationContext appContext = new AnnotationConfigApplicationContext(RESTConfig.class);
	
	//Request a rest server bean from the appContext containing the information needed to access the rest servive.
	
	RESTServer t_restServer = appContext.getBean(RESTServer.class);
	
	//Map data to be sent to server.
	
	Map<String, String> mapData = new HashMap<String, String>();
    mapData.put("id", "INID");
    
    //Trying posting and returning a user.
   
    public void postUser() {
    	try {
    		RestTemplate restTemplate = new RestTemplate();
    		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

         	String uri = new String("http://" + t_restServer.getHost() + ":8080/springmvc-resttemplate-test/api/{id}");

         	User t_User = new User();
         	t_User.setName("L Newcombe");
         	t_User.setUser("LN");


         	User returns = restTemplate.postForObject(uri, t_User, User.class, mapData);
         	
         	log.debug("User: " + t_User.toString());
    	}
    	catch(HttpClientErrorException error) {
    		//If there is a HTTP exception then display an error message.
    		
    		log.error("Error: " + error.getResponseBodyAsString());
    		
    		ObjectMapper map = new ObjectMapper();
    		ErrorLog logError = map.readValue(error.getResponseBodyAsString(), ErrorLog.class);
    		
    		log.error("Error: " + loggError.getErrorMessage());
    		
    	}
    	catch(Exception error) {
    		log.error("Error: " + error.getMessage());
    	}
    }
}
