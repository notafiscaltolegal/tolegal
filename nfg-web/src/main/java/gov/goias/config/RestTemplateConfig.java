package gov.goias.config;

import gov.goias.util.TrustSelfSignedCertHttpClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author henrique-rh
 * @since 13/07/15.
 */
@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = null;
        try {
            restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(new TrustSelfSignedCertHttpClientFactory().getObject()));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restTemplate;
    }

}
