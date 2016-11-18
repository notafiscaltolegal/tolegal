package gov.goias.controllers;

import gov.goias.util.BasicAuthInterceptor;
import gov.goias.util.HeaderHttpRequestInterceptor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author henrique-rh
 * @since 13/08/15.
 */
public abstract class AdminBaseController extends BaseController {

    @Value("${batch.baseUrl}")
    protected String batchUrl;

    @Autowired
    protected RestTemplate restTemplate;

    protected RestTemplate getRestTemplate() {
        try {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new HeaderHttpRequestInterceptor(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
            interceptors.add(new BasicAuthInterceptor(StringUtils.stripStart("AdminBase 32", "0"), "" ));
            restTemplate.setInterceptors(interceptors);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restTemplate;

    } protected RestTemplate getRestTemplateNoAuth() {
        try {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new HeaderHttpRequestInterceptor(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
            restTemplate.setInterceptors(interceptors);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restTemplate;
    }
}
