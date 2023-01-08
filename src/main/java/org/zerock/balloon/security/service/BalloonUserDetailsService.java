package org.zerock.balloon.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.repository.MemberRepository;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BalloonUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //아이디로 DB에 있는 정보 가져와서 DTO리턴
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("BalloonUserDetailsService loadUserByUsername.........id: " + username);

        Optional<Member> result = memberRepository.findById(username, false);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("...................아이디 혹은 소셜가입 여부를 체크해주세요.");
        }
        Member member = result.get();
        log.info("로그인 시도하는 유저 DB 정보....................: " + member);

        BalloonAuthMemberDTO balloonAuthMember = new BalloonAuthMemberDTO(
                member.getId(),
                member.getPw(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                member.isSns(),
                member.getRegDate(),
                member.getRegDate(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
        );
        //??????
        log.info("로그인 시도하는 유저 DTO 정보....................: " + balloonAuthMember);

        return balloonAuthMember;
    }


//    public void loginUser(boolean login){
//        AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
//        if (trustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
//            login = false;
//        }
//        else {
//            login = true;
//        }
//    }
}
