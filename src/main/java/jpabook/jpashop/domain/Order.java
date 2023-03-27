package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")     //테이블 이름을 orders로 지정한다.
@Getter @Setter
//@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch= LAZY) // Order가 다수 이고 Member가 1이기 때문에 ManyToOne이다.
    @JoinColumn(name = "member_id")     // 매핑을 member_id로 할거다 라고 forignkey(외래키) 이름을 정해준다.
    // 이제 연관관계의 주인을 정해야 하는데 외래키가 가까운 곳(있는곳)을 주인으로 정하는게 좋다.
    // 주인은 외래키 만 설정해주면 되고
    private Member member;

    @OneToMany(mappedBy = "order", cascade= CascadeType.ALL)  // OrderItem 안에 있는 order와 매핑
    private List<OrderItem> orderItems = new ArrayList<>();
    // cascade = CascadeType.ALL : 오더아이템스에 데이터만 넣어두고 오더를 저장하면 오더아이템도 같이 저장이 된다.
    // 원래는 오더를 저장하고 오더아이템스를 따로 저장을 했어야 했는데 // 삭제할 때도 다같이 삭제된다.

    @OneToOne(fetch= LAZY, cascade= CascadeType.ALL)       // 1대1 상태에서 외래키를 아무곳에 설정해도 상관없다. 하지만 엑세스가 많은 곳에 두는게 더 좋다.
    @JoinColumn(name = "delivery_delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;         // 주문 상태 (ORDER, CANCEL) 이렇게 2가지를 넣을 것이다.

    //== 연관관계 메소드 ==//
    public void setMember(Member member){       //member와 order를 설정
        this.member = member;
        member.getOrder().add(this);
    }
    
    public void addOrderItem(OrderItem orderItem){  // order와 orderitem를 설정
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDevlivery(Delivery delivery){    // order와 Delivery를 설정
        this.delivery = delivery;
        delivery.setOrder(this);
    }
//--------------------------------------------------------------------------------------------------------------------
    //== 생성 메소드 ==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {    // ...은 여러개를 넘길수 있는 문법이다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);         // 이게 orderStauts를 처음 상태로 order에 강제를 넣는 문법이다.
        order.setOrderDate(LocalDateTime.now());    // 현재시간으로 잡는다. -> 주문시간 정보
        return order;
    }



    //== 비즈니스 로직 ==//

    //주문 취소
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {  // 만약 배송을 했으면 취소가 불가능하기 때문에 if문 사용
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();     // 오더 아이템에도 캔슬을 해줘야 한다. 오더 한번 주문을 할 때 한 회원이 두개를 주문할 수 도 있기 때문에
        }
    }

    //== 죄회 로직 ==//

    // 전체 주문가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
