package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 어딘가에 내장될수 있다는 뜻이다.
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {   // jpa 특성상 리플랙셕이나, 프록시를 써야하는 경우가 많아서 기본 생성자를 만들어 줘야 한다.
    }   // public으로 하면 공개적이여서 사람들이 많이 호출할 수 있기 때문에, protected까지는 허용한다.

    public Address(String city, String street, String zipcode) {
        this.city=city;
        this.street=street;
        this.zipcode=zipcode;
    }   // 값 타입은 기본적으로 변경이 되면 안된다. 그래서 생성할 때만 값을 입력 할 수 있게 public으로 만들어주고 @Setter를 사용하지 않는게 좋다.
}
