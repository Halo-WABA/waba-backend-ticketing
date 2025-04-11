package com.festimap.tiketing.global.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class SmsConfig {

    @Bean(name = "simpleRestTemplate")
    public RestTemplate smsRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "pooledRestTemplate")
    public RestTemplate pooledRestTemplate() {

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(30);
        cm.setDefaultMaxPerRoute(30);

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        factory.setConnectionRequestTimeout(3000);

        return new RestTemplate(factory);
    }

}
