package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")    // 싱글테이블에서 저장할 때 구분하기 위해 사용한다. 책이면 B
@Getter
@Setter
public class Book extends Item{

    private String author;  // 저자
    private String isbn;    // 기타정보

}
