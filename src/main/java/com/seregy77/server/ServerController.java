package com.seregy77.server;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ServerController implements ServerApi {

  @Override
  public ResponseEntity<ApiResponse> createResource(MultipartFile file, URI callbackUrl) {
    String callback = callbackUrl == null ? null : callbackUrl.toString();
    ApiResponse response = new ApiResponse(
        String.format("Received file '%s' with callback url '%s'",
            file.getOriginalFilename(), callback));

    return ResponseEntity.ok(response);
  }
}
