package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch= LAZY)
    private Order order;

    private Address address;

    @Enumerated(EnumType.STRING)    // 꼭 스트링으로 사용해야 한다. // 중간에 생성되도 값이 밀리지 않는다.
    private DeliveryStatus status; // Ready, Comp

    // Enumerated(EnumType의 디폴트가 ORDINAL)이다. 근데 오디널은 생성된 상태로 1,2,3 순서대로 번호를 매기는데 중간에 xxx등 하나가 생기게 되면 값이 밀리게 된다. 그래서 절대 사용하면 안된다.
}
