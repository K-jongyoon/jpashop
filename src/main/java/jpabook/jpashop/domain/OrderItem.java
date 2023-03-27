package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성 로직을 public으로 받아오게되면 코드가 꼬일 가능성이 높다. and static 생성 메도를 사용하는게 좋기 때문에 public을 방지하고 아규먼트를 사용했다.
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;


    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;     // 주문 가격
    private int count;          // 주문 수량
//--------------------------------------------------------------------------------------------------------------------

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);    // 제고를 감소
        return orderItem;
    } // 가격이 변동 될 수도 있거나, 쿠폰이나 할인이 있을 수 도 있기 때문에 따로 작성하는게 좋다.

    //== 비즈니스 로직 ==//
    public void cancel() {  // 오더 아이템에도 캔슬을 해줘야 한다. 오더 한번 주문을 할 때 한 회원이 두개를 주문할 수 도 있기 때문에
        getItem().addStock(count);  // 제고 수량을 원복한다.
    }

    //== 조회 로직 ==//
    // 주문상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
