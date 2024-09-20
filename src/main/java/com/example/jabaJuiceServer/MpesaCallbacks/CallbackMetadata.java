package com.example.jabaJuiceServer.MpesaCallbacks;

import java.util.List;

public class CallbackMetadata {
    private List<CallbackMetadataItem> Item;

    // Getters and setters

    public CallbackMetadata() {
    }

    public List<CallbackMetadataItem> getItem() {
        return Item;
    }

    public void setItem(List<CallbackMetadataItem> item) {
        Item = item;
    }
}
