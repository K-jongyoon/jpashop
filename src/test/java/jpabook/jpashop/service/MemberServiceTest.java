package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long saveId=memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(saveId)); // 멤버 리파지토리에서 찾아온 이름과 맴버에 저장되어있는 이름이 같으면 회원가입 성공
    }

    @Test //(expected = IllegalStateException.class)
    public void 중복_회원_예제() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        // when
        memberService.join(member1);
        memberService.join(member2);    // 위에 (expcted = IllegalStateException.class)를 하면 try문을 안써도 된다.
        // then
        fail("에외가 발생해야 된다.");
    }

}