package org.zerock.balloon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.MemberRole;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("테스트멤버 삽입")
    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,200).forEach(i->{
            Member member= Member.builder().id("id"+i).pw(passwordEncoder.encode("1111")).name("name"+i).email("email"+i+"@email.com").nickname("nickname" + i).sns(false).build();
            member.addMemberRole(MemberRole.USER);
            if(i>45){
                member.addMemberRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }
}