package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // 매핑명이 member_id기 때문에 Column을 준다.
    private Long id;    // id

    private String name;    // 회원명

    @Embedded
    private Address address;
    // 내장 타입을 포함했기 때문에 어노테이션으로 표현한다. // Embedded와 Embeddable 둘중 하나만 써도 되는데 둘다 쓰는게 좋다

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> order = new ArrayList<>();
    // Member 1이고 Order은 다수이기 때문에 OneToMany이다. // 하나의 회원이 여러개의 상품을 주문하기 때문이다.
    // (mappedBy = "member") = mappedBy = 나는 연관관계의 거울이에요 주인이 아니에요 라는 뜻이다. // 오더 테이블에 있는 member 필드에 의해 매핑이 되었단 뜻이다.

}
