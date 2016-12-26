package gov.goias.util;

import gov.goias.exceptions.NFGException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.Callable;

/**
 * Created by lucas-mp on 16/04/15.
 */
public class UriCallable implements Callable<String> {
    private HttpUriRequest request;
    private HttpClient client;

    public UriCallable(HttpUriRequest request
//            , HttpClient client
    ) {
        super();
        this.request = request;
        this.client = new DefaultHttpClient();
    }

    public String call() throws Exception {
        try{
            HttpResponse httpResponse = client.execute(request);

            HttpEntity responseEntity = httpResponse.getEntity();
            String retorno = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);

            return retorno;
        }catch (HttpHostConnectException hhce){
            hhce.printStackTrace();
            throw new NFGException("Erro ao tentar realizar call de serviço.");
        }
    }
}