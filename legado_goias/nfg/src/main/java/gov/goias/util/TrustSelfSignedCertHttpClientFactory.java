package gov.goias.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.FactoryBean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class TrustSelfSignedCertHttpClientFactory implements FactoryBean<HttpClient> {

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public Class<?> getObjectType() {
    return HttpClient.class;
  }

  @Override
  public HttpClient getObject() throws Exception {
    SSLContext ctx = SSLContext.getInstance("SSL");
    X509TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
      public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
      public X509Certificate[] getAcceptedIssuers() {return null;}
    };
    ctx.init(null, new TrustManager[]{tm}, null);
    SSLContext.setDefault(ctx);
    SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    // based on HttpClients.createSystem()
    return HttpClientBuilder.create()
      .useSystemProperties()
      .setSSLSocketFactory(sslConnectionSocketFactory)  // add custom config
      .build();
  }

}