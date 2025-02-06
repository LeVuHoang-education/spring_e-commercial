package dev.backend.ecommercial.model.dto;

import dev.backend.ecommercial.model.entity.Address;
import dev.backend.ecommercial.model.entity.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private Long addressId;
    private String streetLine;
    private Long userId;

    public static AddressDto from(Address address) {
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .streetLine(address.getAddressLine())
                .userId(address.getUser().getUserId())
                .build();
    }
}
