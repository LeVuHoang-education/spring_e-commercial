package dev.backend.ecommercial.model.payload.request;

import lombok.Data;

@Data
public class AddAddress {
    private Long userId;
    private String addressLine;

}
