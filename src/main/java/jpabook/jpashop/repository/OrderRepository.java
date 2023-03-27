package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.hibernate.Criteria;
import org.springframework.data.annotation.Persistent;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderRepository {

    private final EntityManager em;

    public OrderRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Order order) {
        em.persist(order);
    }

    // 단권 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order  o join o.member m" +
//                " where o.status = :status" + " and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())       // 파라미터 바인딩
//                .setParameter("name", orderSearch.getMamberName())          // 파라미터 바인딩
//                .setMaxResults(1000)    // 최대 1000개 까지만 조회할수 있다.
//                .getResultList();
//    }    // 검색기능 로직
    
    /*
    JPA 의 동적 쿼리문
    1. JPA의 표준스텍인 = JPA Criteria 의 단점 => 유지보수성이 엄청 안좋다
    2. 쿼리문을 직접 다 작성하는건 너무 복잡하고
    3. 쿼리 DSL 을 사용하면 간편하다.
     */

    // JPA Criteria
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMamberName())) {
            Predicate name=cb.like(m.<String>get("name"), "%" + orderSearch.getMamberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

//    // jpql 직접 코드 짜기
//    public List<Order> findAllByString(OrderSearch orderSearch) {
//        String jpql = "select o from Order o join o.member";
//        boolean isFirstCondition = true;
//
//        // 주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " o.status = :status";
//        }
//
//        // 회원 이름 검색
//        if (StringUtils.hasText(orderSearch.getMamberName())){
//            if (isFirstCondition){
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " m.name like :name";
//        }
//
//        TypedQuery<Order> query=em.createQuery(jpql, Order.class)
//                .setMaxResults(1000);// 최대 1000까지만 조회가
//
//        if (orderSearch.getOrderStatus() != null) {
//            query = query.setParameter("status", orderSearch.getOrderStatus());
//        }
//        if (StringUtils.hasText(orderSearch.getMamberName())) {
//            query = query.setParameter("name", orderSearch.getMamberName());
//        }
//
//        return query.getResultList();
//    }

//    // Querydsl로 jpa 동적 처리하기
//    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder order = QOrder.order;
//        QMember member = QMember.member;
//
//
//    }
}
