package com.festimap.tiketing.infra.sms.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        WireMockServer server = new WireMockServer(
                WireMockConfiguration.options()
                        .port(8089)
                        .notifier(new Slf4jNotifier(true))
        );

        server.stubFor(WireMock.post(WireMock.urlEqualTo("/sms/send"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withFixedDelay(300) // 300ms 지연 설정
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"message\": \"SMS sent successfully\" }")));

        return server;
    }
}