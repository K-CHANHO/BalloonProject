package org.zerock.balloon.security.service;

//소셜 로그인 처리가 끝난 결과 가져오는 작업

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.MemberRole;
import org.zerock.balloon.repository.MemberRepository;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BalloonOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //소셜 로그인 시도 -> 회원가입처리(DB저장) 후 DTO리턴 (기존 회원은 DTO리턴)
    //OAuth2UserRequest는 현재 어떤 서비스를 통해서 로그인이 이루어졌는지 알아내고 전달된 값을 추출할 수 있는 데이터를
    //Map<String, Object> 형태로 사용
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("로그인 loadUser userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("로그인 시 사용 SNS: " + clientName);
        log.info(userRequest.getAdditionalParameters());
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
//                .getUserInfoEndpoint().getUserNameAttributeName();
//        log.info("뭐쟈..." + userNameAttributeName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("................로그인 사용자 정보................");
        log.info(oAuth2User);

        oAuth2User.getAttributes().forEach((key, value)->{
            log.info(key + ": " + value);
        });

        String email = null;

        //구글을 사용한 경우
        if(clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
            log.info("구글로 로그인 한 소셜 email: " + email);
        }
        if(clientName.equals("Naver")) {
            int i = oAuth2User.getAttributes().get("response").toString().lastIndexOf("=");
            email = oAuth2User.getAttributes().get("response").toString().replaceAll("}","").substring(i+1);
            log.info("네이버로 로그인 한 소셜 email: " + email);
        }
        if(clientName.equals("Kakao")){
            int i = oAuth2User.getAttributes().get("kakao_account").toString().lastIndexOf("=");
            email = oAuth2User.getAttributes().get("kakao_account").toString().replaceAll("}","").substring(i+1);
            log.info("카카오로 로그인 한 소셜 email: " + email);
        }


        //소셜 로그인 사용자 회원가입 처리 후 DB정보 가져옴(기존 회원은 그냥 DB정보 가져옴)
        Member member = saveSocialMember(email);
        BalloonAuthMemberDTO balloonAuthMemberDTO = new BalloonAuthMemberDTO(
                member.getId(),
                member.getPw(),
                member.getName(),
                member.getEmail(),
                member.getNickname(),
                true,
                member.getRegDate(),
                member.getModDate(),
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_"+role.name())
                ).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        //entitytodto든 dtotoentity든 순서 지켜서 작성할 것
        log.info("소셜 멤버 entitytoDTO 한 거 보여줘: " + balloonAuthMemberDTO);
        return balloonAuthMemberDTO;
    }

    //소셜 로그인 사용자 자동 회원가입 처리
    private Member saveSocialMember(String email){

        //기존에 동일한 이메일로 가입한 회원이 있는 경우 조회만
        Optional<Member> result = memberRepository.findByEmail(email, true);
        if(result.isPresent()){
            log.info("DB에서 가져온 소셜 로그인 회원 정보:" + result.get());
            return result.get();
        }

        //없으면 회원추가
        Member member = Member.builder().
                id(createRandomId())
                .pw(passwordEncoder.encode(createRandomId()))
                .sns(true)
                .email(email)
                .name(createRandomId())
                .nickname(createRandomId())
                .build();
        member.addMemberRole(MemberRole.USER);
        memberRepository.save(member);
        return member;
    }

    //랜덤 아이디 생성 메서드
    private String createRandomId(){
        char idCollection[] = new char[] {
                '1','2','3','4','5','6','7','8','9','0',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};//배열에 선언

        String randomId = "";

        for (int i = 0; i < 10; i++) {
            int selectRandomId = (int)(Math.random()*(idCollection.length));//Math.rondom()은 0.0이상 1.0미만의 난수 생성
            randomId += idCollection[selectRandomId];
        }
        return randomId;
    }





}
