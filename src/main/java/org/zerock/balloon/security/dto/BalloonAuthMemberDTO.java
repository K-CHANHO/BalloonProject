package org.zerock.balloon.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

//인증을 거치는 거니...?
//MemberDTO같은 역할
@Log4j2
@Getter
@Setter
@ToString
public class BalloonAuthMemberDTO extends User implements OAuth2User { //Spring Security (일반 로그인 & 소셜 로그인)

    private String id;

    private String pw;

    private String name; //이름

    private String email; //이메일

    private String nickname; //닉네임

    private boolean sns; //소셜 로그인 여부

    private LocalDateTime regDate, modDate; //등록일, 수정일

    private Map<String, Object> attribute;


    //아이디, 비밀번호, 권한
    public BalloonAuthMemberDTO(String username,
                                String password,
                                String name,
                                String email,
                                String nickname,
                                boolean sns,
                                LocalDateTime regDate,
                                LocalDateTime modDate,
                                Collection<? extends GrantedAuthority> authorities){

        super(username, password, authorities);
        this.id = username;
        this.pw = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.sns = sns;
        this.regDate = regDate;
        this.modDate = modDate;

    }

    //OAuth2User 인증 결과 가져오기(?) OAuth2User -> BalloonAuthMemberDTO
    public BalloonAuthMemberDTO(String username,
                                String password,
                                String name,
                                String email,
                                String nickname,
                                boolean sns,
                                LocalDateTime regDate,
                                LocalDateTime modDate,
                                Collection<? extends GrantedAuthority> authorities,
                                Map<String, Object> attribute){

        super(username, password, authorities);
        this.id = username;
        this.pw = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.sns = sns;
        this.regDate = regDate;
        this.modDate = modDate;
        this.attribute = attribute;
    }

    //OAuth2User에서 정보 가져오기
    @Override
    public Map<String, Object> getAttributes() {
        return this.attribute;
    }


}
