package ca.corefacility.bioinformatics.irida.repositories.remote.oltu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import ca.corefacility.bioinformatics.irida.exceptions.IridaOAuthException;

public class IridaOAuthErrorHandler extends DefaultResponseErrorHandler{
	private static final Logger logger = LoggerFactory.getLogger(IridaOAuthErrorHandler.class);
	
	private URI service;

	public void handleError(ClientHttpResponse response) throws IOException{
		
		HttpStatus statusCode = response.getStatusCode();
		System.out.println("handling " + statusCode.toString());
		switch(statusCode){
			case UNAUTHORIZED:
				throw new IridaOAuthException("User is unauthorized for this service", service);
			default:
				super.handleError(response);
		}
	}
	
	public void setService(URI service){
		this.service = service;
	}
}
