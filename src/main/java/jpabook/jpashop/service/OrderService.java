package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;    // 주문로직에서 필요
    private final ItemRepository itemRepository;        // 주문로직에서 필요

    // 주문
    @Transactional // 주문은 데이터를 변경하기 때문에 Transactional이 필요
    public Long order(Long memberId, Long itemId, int count) {
        
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);       // memberId 한개만 조회
        Item item = itemRepository.findOne(itemId);               // itemId 한개만 조회
        
        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());   // 회원 주소에 있는 주소로만 배송
        delivery.setStatus(DeliveryStatus.READY);

        
        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);      // 오더 아이템을 static 생성 메소드를 통해 생성

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);       // 주문도 static 생성 메소드를 통해 생성
        
        // 주문 저장
        orderRepository.save(order);        // cascade 덕에 이렇게 하나만 저장을 해줘도 오더 아이템이랑, 딜리버리가 자동으로 퍼시스트 됐다. // cascade는 참조하게 주인이 프라이빗 오너인 경우에만 써야 한다.   // 만약 다른 곳에서 딜리버리를 많이 참조하면 사용하면 안된다.
        return order.getId();               // 요번경우는 오더만 딜리버리사용하고, 오더만 오더아이템을 사용하기 때문에 cascade를 사용했다.
    }

    
    // 취소

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
