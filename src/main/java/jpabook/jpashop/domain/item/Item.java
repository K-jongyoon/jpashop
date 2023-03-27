package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑이기 때문에 상속관계 전략을 세우는 어노테이션이다.  //JOINED(정규화된 스타일) // SINGLE_TABLE(한테이블에 다 떄려박는것) //TABLE_PER_CLASS(책, 영화, 앨범 3테이블만 나오는 것이다.)
@DiscriminatorColumn(name = "ditype")   // 구분할때 // @DiscriminatorValue랑 같이 사용하는데 싱글테이블이니까 저장할때 구분하기 위해 사용
@Getter @Setter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
//--------------------------------------------------------------------------------------------------------------------
    //=== 비즈니스 로직 ===//
    
    // 제고 슈량을 증가하는 로직
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 제고 수량을 감소하는 로직
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");  // 0보다 제고가 없게 되면 에러를 띄운다.
        }
        this.stockQuantity = restStock;
    }
}
