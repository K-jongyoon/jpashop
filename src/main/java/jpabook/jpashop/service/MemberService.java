package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)  // jpa의 모든 데이터 변경이나 로직들은 트랜잭션 안에서 실행되어야 한다.
// readOnly = true는 jpa가 조회하는 곳에서는 성능을 최적화 한다.    // readOnly = true는 쓰기에서는 쓰면 안된다. 데이터 변경이 안되기 때문이다.
@RequiredArgsConstructor // final이 있는 필드만 가지고 생성자를 만들어 준다.
public class MemberService {

    /* Autowire를 써도 되지만 아래처럼 하는게 단점이 더 적다
    @Autowired
    private MemberRepository memberRepository;    */

    private final MemberRepository memberRepository;

    /* 하지만 이거보다 더 좋은 방법은 lombok에 있는 @RequiredArgsConstructor를 쓰는 것이다.
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }     */

    // 회원 가입
    @Transactional // 전체 에서는 읽기만 가능하게 하고 Transcational의 디폴트는 readOnly = false 이기 때문에 데이터를 쓰는 곳에만 Transcational을 열어두는게 좋다.
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // 중복회원 방지를 위한 메소드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    // @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 한 회원만 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
