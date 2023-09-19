package com.seregy77.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.binaryEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.seregy77.server.ApiResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class ServerApiClientTest {

  private static final String API_PATH = "/server/resources";
  private static final String SERVER_RESPONSE_JSON = "{\"value\":  \"some-message\"}";
  private static final byte[] FILE_CONTENT = "mock-content".getBytes(StandardCharsets.UTF_8);

  @RegisterExtension
  static WireMockExtension serverWireMock = WireMockExtension.newInstance()
      .options(WireMockConfiguration.wireMockConfig().dynamicPort())
      .build();

  @Autowired
  private ServerApiClient serverApiClient;

  @Test
  void sendRequest_withoutOptionalUri() {
    MockMultipartFile multipartFile = new MockMultipartFile("mock-file", FILE_CONTENT);
    serverWireMock.stubFor(post(urlPathEqualTo(API_PATH))
        .willReturn(jsonResponse(SERVER_RESPONSE_JSON, HttpStatus.CREATED.value())));

    ResponseEntity<ApiResponse> response = serverApiClient.createResource(multipartFile, null);

    assertTrue(response.getStatusCode().is2xxSuccessful());
    serverWireMock.verify(postRequestedFor(urlEqualTo(API_PATH))
        .withRequestBodyPart(aMultipart("file").withBody(binaryEqualTo(FILE_CONTENT)).build()));
  }

  @Test
  void sendRequest_withOptionalUri() {
    MockMultipartFile multipartFile = new MockMultipartFile("mock-file", FILE_CONTENT);
    serverWireMock.stubFor(post(urlPathEqualTo(API_PATH))
        .willReturn(jsonResponse(SERVER_RESPONSE_JSON, HttpStatus.CREATED.value())));
    String callbackValue = "http://127.0.0.1";

    ResponseEntity<ApiResponse> response = serverApiClient.createResource(multipartFile,
        URI.create(callbackValue));

    assertTrue(response.getStatusCode().is2xxSuccessful());
    serverWireMock.verify(postRequestedFor(urlEqualTo(API_PATH))
        .withRequestBodyPart(aMultipart("file").withBody(binaryEqualTo(FILE_CONTENT)).build())
        .withRequestBodyPart(aMultipart("callbackUrl").withBody(equalTo(callbackValue)).build()));
  }

  @DynamicPropertySource
  static void initClientProperties(DynamicPropertyRegistry registry) {
    registry.add("app.client.url", serverWireMock::baseUrl);
  }
}
