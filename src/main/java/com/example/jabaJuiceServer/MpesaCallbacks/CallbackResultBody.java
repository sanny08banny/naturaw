package com.example.jabaJuiceServer.MpesaCallbacks;

import com.example.jabaJuiceServer.entities.MpesaResult;

public class CallbackResultBody {
    private MpesaResult stkCallback;

    public CallbackResultBody() {
    }

    public MpesaResult getStkCallback() {
        return stkCallback;
    }

    public void setStkCallback(MpesaResult stkCallback) {
        this.stkCallback = stkCallback;
    }
}
