package kr.co.gpgp.domain.delivery;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import kr.co.gpgp.domain.address.Address;
import kr.co.gpgp.domain.delivery.dto.DeliveryResponse;
import kr.co.gpgp.domain.requirement.Requirement;
import kr.co.gpgp.domain.user.Role;
import kr.co.gpgp.domain.user.User;
import kr.co.gpgp.repository.address.AddressJpaRepository;
import kr.co.gpgp.repository.delivery.DeliveryJpaRepository;
import kr.co.gpgp.repository.user.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
public class DeliveryUserServiceTest {

    private User user;
    private Address address;
    private Requirement requirement;
    private Delivery delivery;

    @SpyBean
    public DeliveryUserService deliveryUserService;

    @Autowired
    public AddressJpaRepository addressRepository;

    @Autowired
    public DeliveryJpaRepository deliveryRepository;

    @Autowired
    public UserJpaRepository userRepository;

    @BeforeEach
    void setup() {

        user = User.of("AAA", "AAA@gmail.com", Role.USER);
        address = Address.of(user, "AAA_AAA_AAA", "12345", "AAAName", "AAA1");
        requirement = new Requirement("AAA");
        delivery = Delivery.of(requirement, address);

        user = userRepository.save(user);
        address = addressRepository.save(address);
        deliveryRepository.save(delivery);

    }

    @Test
    void 모든배송_조회_성공() {
        //given
        Address address2 = Address.of(user, "AAA2_AAA2_AAA2", "12345", "AAA@Name", "AAA2");
        Requirement requirement2 = new Requirement("AAA2");
        Delivery delivery2 = Delivery.of(requirement2, address2);
        addressRepository.save(address2);
        deliveryRepository.save(delivery2);

        User unknownUser = User.of("BBB", "BBB@gmail.com", Role.USER);
        Address unknownAddress = Address.of(unknownUser, "BBB_BBB_BBB", "12345", "BBBName", "BBB1");
        Delivery unknownDelivery = Delivery.of(new Requirement("BBB"), unknownAddress);

        userRepository.save(unknownUser);
        addressRepository.save(unknownAddress);
        deliveryRepository.save(unknownDelivery);

        //when
        List<Delivery> list = deliveryUserService.selectAll(user.getId());

        //then
        assertThat(list.stream().count()).isEqualTo(2);
        Assertions.assertAll(
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.get(0).getRequirement()).isEqualTo(requirement.getMessage()),
                () -> assertThat(list.get(0).getAddressRoadName()).isEqualTo(address.getRoadName()),
                () -> assertThat(list.get(0).getAddressZipCode()).isEqualTo(address.getZipCode()),
                () -> assertThat(list.get(0).getAddressName()).isEqualTo(address.getName()),
                () -> assertThat(list.get(0).getAddressDetailed()).isEqualTo(address.getDetailed())
        );
        Assertions.assertAll(
                () -> assertThat(list).isNotNull(),
                () -> assertThat(list.get(1).getRequirement()).isEqualTo(requirement2.getMessage()),
                () -> assertThat(list.get(1).getAddressRoadName()).isEqualTo(address2.getRoadName()),
                () -> assertThat(list.get(1).getAddressZipCode()).isEqualTo(address2.getZipCode()),
                () -> assertThat(list.get(1).getAddressName()).isEqualTo(address2.getName()),
                () -> assertThat(list.get(1).getAddressDetailed()).isEqualTo(address2.getDetailed())
        );

    }

    @Test
    void ID가_존재하지않아_모든배송_조회_실패() {
        org.assertj.core.api.Assertions
                .assertThatThrownBy(() -> deliveryUserService.selectAll(Long.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유저 ID가 없어 배송을 조회할수 없습니다.");
    }

    @Test
    void 선택_배송_조회_성공() {

        User pickUser = User.of("BBB", "BBB@gmail.com", Role.USER);
        Address pickAddress = Address.of(pickUser, "BBB_BBB_BBB", "12345", "BBBName", "BBB1");
        Requirement pickRequirement = new Requirement("BBB");
        Delivery pickDelivery = Delivery.of(pickRequirement, pickAddress);

        userRepository.save(pickUser);
        addressRepository.save(pickAddress);
        deliveryRepository.save(pickDelivery);

        DeliveryResponse deliveryResponse = deliveryUserService.select(pickUser.getId(), pickDelivery.getId());

        Assertions.assertAll(
                () -> assertThat(deliveryResponse).isNotNull(),
                () -> assertThat(deliveryResponse.getRequirement()).isEqualTo(pickRequirement.getMessage()),
                () -> assertThat(deliveryResponse.getAddressName()).isEqualTo(pickAddress.getName()),
                () -> assertThat(deliveryResponse.getDetailedAddress()).isEqualTo(pickAddress.getDetailed()),
                () -> assertThat(deliveryResponse.getRoadName()).isEqualTo(pickAddress.getRoadName()),
                () -> assertThat(deliveryResponse.getZipCode()).isEqualTo(pickAddress.getZipCode())
        );
    }

    @Test
    void 유저ID가_존재하지않아_선택_배송_조회_실패() {

        assertThatThrownBy(() -> deliveryUserService.select(Long.MAX_VALUE, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유저 ID가 없어 배송을 조회할수 없습니다.");
    }

    @Test
    void 유저가_선택한_배송이_유저값이_아니라면_예외가_발생한다() {
        User user = User.of("BBB", "BBB@gmail.com", Role.USER);
        Address address = Address.of(user, "AAA2_AAA2_AAA2", "12345", "AAA@Name", "AAA2");
        Requirement requirement = new Requirement("AAA2");
        Delivery delivery = Delivery.of(requirement, address);
        addressRepository.save(address);
        deliveryRepository.save(delivery);

        assertThatThrownBy(() -> deliveryUserService.select(this.user.getId(), delivery.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("선택한 배송은 유저가 생성한게 아닙니다.");
    }

}