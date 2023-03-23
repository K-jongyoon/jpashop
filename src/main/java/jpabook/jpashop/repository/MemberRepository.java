package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository                                 // spring bin으로 spring 해준다
@RequiredArgsConstructor
public class MemberRepository {

    /*
    @PersistenceContext                     // JPA의 EntityManager를 자동으로 주입해준다
    private EntityManager em;     */
    // Repository에서도 @RequiredArgsConstructor를 사용할 수 있다.
    // EntityManger은 Autowird가 아닌 PersistenceCOntext가 있어야 인젝션이 되는데 // 스프링 부트가 Autowired가 인젝션 될 수 있게 해준다.

    private final EntityManager em;

    // 저장하기 위해 메소드 구현
    public void save(Member member) {
        em.persist(member);             // persist는 영속성 컨텍스트에 member 객체를 넣는다.
    }

    // 아이디 조회 (단건 조회)
    public Member findOne(Long id) {
        return em.find(Member.class, id);   // 기본키를 넣어주면 된다
    }

    // 리스트 조회 (회원목록 등을 확인할 때 필요)
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)       // jpql 구문을 사용
                .getResultList();
    }

    // 이름으로 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)      // 파라미터 바인딩을 해서 특정 이름에 의한 회원만 찾는걸 구현
                .setParameter("name", name)
                .getResultList();   // 이렇게 하면 파라미터가 바인딩 된다.
    }
}
