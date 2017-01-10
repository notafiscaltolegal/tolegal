package gov.goias.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author henrique-rh
 * @since 12/01/15.
 */
public class BasicAuthInterceptor implements ClientHttpRequestInterceptor {

    private static Logger logger = LoggerFactory.getLogger( BasicAuthInterceptor.class );

    private final String username;
    private final String password;

    public BasicAuthInterceptor( String username, String password ) {
        this.username = username;
        this.password = password;
    }

    @Override
    public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution ) throws IOException {

        //Build the auth-header
        final String auth = username + ":" + password;
        final byte[] encodedAuth = Base64.encodeBase64( auth.getBytes( Charset.forName( "US-ASCII" ) ) );
        final String authHeader = "Basic " + new String( encodedAuth );

        //Add the auth-header
        request.getHeaders().add("Authorization", authHeader);

        logger.debug( "Added Basic Authentication Header: user -> {}, password -> {}", username, "******" );

        return execution.execute( request, body );
    }

}