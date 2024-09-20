package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.MyDAOs.PaidOrderRequest;
import com.example.jabaJuiceServer.entities.MpesaRequest;
import com.example.jabaJuiceServer.entities.MpesaResponse;
import com.example.jabaJuiceServer.services.MpesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sanny/lipa-na-mpesa")
public class MpesaController {

    @Autowired
    private MpesaService lipaNaMpesaService;

    @PostMapping("/initiate-stk-push")
    public ResponseEntity<MpesaResponse> initiateSTKPush(@RequestParam("token")String token,
                                                         @RequestBody MpesaRequest request) {
        MpesaResponse response = lipaNaMpesaService.makeMpesaApiCall(request,token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/initiate-stk-push/{phoneNumber}")
    public ResponseEntity<MpesaResponse> initiateSTKPushByPhoneNumber(@PathVariable("phoneNumber")String phoneNumber,
                                                                      @RequestParam("token")String token,
                                                                      @RequestParam("amount")String amount,
                                                                      @RequestParam("pay_link_UI")String pay_link_U) {
        MpesaResponse response = lipaNaMpesaService.makeMpesaApiCallForPhoneNumber(phoneNumber,token,amount,pay_link_U);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/pay-for-delivery")
    public ResponseEntity<MpesaResponse> initiateSTKPushForDeliveryPayment(@RequestBody PaidOrderRequest paidOrderRequest) {
        MpesaResponse response = lipaNaMpesaService.makeMpesaApiCallForDeliveryPayment(paidOrderRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

