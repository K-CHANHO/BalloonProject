package org.zerock.balloon.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.zerock.balloon.entity.Member;
import org.zerock.balloon.entity.MemberRole;
import org.zerock.balloon.security.dto.MemberDTO;
import org.zerock.balloon.security.dto.ResultDTO;

import java.util.stream.Collectors;

public interface MemberService {


    /**
     * 회원가입
     */
    ResultDTO joinMember(MemberDTO memberDTO);


    /**
     * 회원가입 시 중복 체크
     */
    ResultDTO getExistUserId(String userId);
    ResultDTO getExistUserEmail(String userEmail);

    ResultDTO getExistUserNickname(String userNickname);

    /**
     * 회원정보 수정
     */
    ResultDTO changePw(MemberDTO memberDTO);

    ResultDTO changeNickname(MemberDTO memberDTO);

    ResultDTO changeName(MemberDTO memberDTO);

    /**
     * 회원 탈퇴
     */
    void deleteMember(String id);

    static Member memberDTOToEntity(MemberDTO memberDTO){
        Member member = Member.builder()
                .id(memberDTO.getId())
                .pw(memberDTO.getPw())
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .sns(memberDTO.isSns())
                .build();
        member.addMemberRole(MemberRole.USER);
        return member;
    }

    static MemberDTO EntityToMemberDTO(Member member){
        MemberDTO memberDTO = MemberDTO.builder()
                .id(member.getId())
                .pw(member.getPw())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .sns(member.isSns())
                .build();
        return memberDTO;
    }

}
