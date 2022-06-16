package kr.co.gpgp.web.api.delivery;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.gpgp.domain.address.Address;
import kr.co.gpgp.domain.delivery.Delivery;
import kr.co.gpgp.domain.requirement.Requirement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryResponse {

    private Long id;
    private String requirement;
    private String roadName;
    private String zipCode;
    private String addressName;
    private String detailedAddress;

    @QueryProjection
    public DeliveryResponse(Long id, String requirement, String roadName, String zipCode, String addressName, String detailedAddress) {
        this.id = id;
        this.requirement = requirement;
        this.roadName = roadName;
        this.zipCode = zipCode;
        this.addressName = addressName;
        this.detailedAddress = detailedAddress;
    }

    public static DeliveryResponse of(Long id, String requirement, String roadName, String zipCode, String addressName, String detailedAddress) {
        return new DeliveryResponse(id, requirement, roadName, zipCode, addressName, detailedAddress);
    }

    public static DeliveryResponse of(Long id, Requirement requirement, Address address) {
        return new DeliveryResponse(id, requirement.getMessage(), address.getRoadName(), address.getZipCode(), address.getName(), address.getDetailed());
    }

    public static DeliveryResponse of(Delivery delivery) {
        System.out.println(delivery.getRequirement());
        return new DeliveryResponse(delivery.getId(), delivery.getRequirement().getMessage(), delivery.getAddressRoadName(), delivery.getAddressZipCode(), delivery.getAddressName(), delivery.getAddressDetailed());
    }

    public Delivery toEntity(Requirement requirement, Address address) {
        return Delivery.of(requirement, address);
    }

    public static List<DeliveryResponse> convertList(List<Delivery> list) {
        return list.stream()
                .map(DeliveryResponse::of)
                .collect(Collectors.toList());
    }
}