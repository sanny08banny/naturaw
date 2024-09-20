package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.MpesaCallbacks.CallbackMetadata;

import java.util.List;

public class MpesaResult {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private CallbackMetadata CallbackMetadata;

    public MpesaResult() {
    }

    public String getMerchantRequestID() {
        return MerchantRequestID;
    }

    public void setMerchantRequestID(String merchantRequestID) {
        MerchantRequestID = merchantRequestID;
    }

    public String getCheckoutRequestID() {
        return CheckoutRequestID;
    }

    public void setCheckoutRequestID(String checkoutRequestID) {
        CheckoutRequestID = checkoutRequestID;
    }

    public int getResultCode() {
        return ResultCode;
    }

    public void setResultCode(int resultCode) {
        ResultCode = resultCode;
    }

    public String getResultDesc() {
        return ResultDesc;
    }

    public void setResultDesc(String resultDesc) {
        ResultDesc = resultDesc;
    }

    public CallbackMetadata getCallbackMetadata() {
        return CallbackMetadata;
    }

    public void setCallbackMetadata(CallbackMetadata callbackMetadata) {
        CallbackMetadata = callbackMetadata;
    }
}