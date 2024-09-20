package com.example.jabaJuiceServer.MpesaCallbacks;

import com.example.jabaJuiceServer.entities.MpesaResponse;
import com.example.jabaJuiceServer.entities.MpesaResult;
import com.example.jabaJuiceServer.services.MpesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sanny/callback")
public class MpesaCallbackController {
    private MpesaService mpesaService;

    @Autowired
    public MpesaCallbackController(MpesaService mpesaService) {
        this.mpesaService = mpesaService;
    }

    @PostMapping("/response")
    public ResponseEntity<String> handleSuccessCallback(@RequestBody MpesaResponse response) {
        // Handle success callback response here
        // You can access response fields using response.getMerchantRequestID(), response.getCheckoutRequestID(), etc.
        return ResponseEntity.ok("Success callback handled");
    }

    @PostMapping
    public ResponseEntity<String> handleResultCallback(@RequestBody Map<String, Object> responseBody) {
        // Handle result callback response here
        Object body = responseBody.get("Body");
        if (body instanceof Map) {
            Map<String, Object> resultBody = (Map<String, Object>) body;
            mpesaService.handleResultCallback(resultBody);
            return ResponseEntity.ok("Result callback handled");
        } else {
            return ResponseEntity.badRequest().body("Invalid response format");
        }
        // You can access response fields using response.getBody().getStkCallback().getMerchantRequestID(), etc.
    }
}
