package org.zerock.balloon.security.handler;
//로그인 성공 후 처리 담당(소셜 로그인만 해당되네...)

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.balloon.security.dto.BalloonAuthMemberDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Log4j2
public class BalloonLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    //소셜 로그인 회원은 회원정보 수정 페이지로 넘어가게 처리... 미완성
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 핸들러 진입...................onAuthenticationSuccess");

//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
//        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
//        response.setHeader("Expires", "0"); // Proxies.

        //쿠키 생성
//        Cookie cookie = new Cookie("loginCookie", "Success");
//
//        // 쿠키 값 재설정
////        cookie.setValue("value2");
//
//        //쿠키 유지 시간 설정(초단위)
//        cookie.setMaxAge(60*60*24*7);
//
//        // 쿠키를 클라이언트로 전송
//        response.addCookie(cookie);

        BalloonAuthMemberDTO authMemberDTO = (BalloonAuthMemberDTO)authentication.getPrincipal();
        log.info("로그인 한 소셜 멤버 정보: " + authMemberDTO);

        boolean sns = authMemberDTO.isSns();
        log.info(sns);
        LocalDateTime modDate = authMemberDTO.getModDate();
        LocalDateTime regDate = authMemberDTO.getRegDate();
        log.info("회원정보 수정이 필요한 유저입니까?" + sns);

        //소셜로그인 회원이고, 회원정보 등록일과 수정일이 같은 사람이면 회원정보 수정 페이지로 이동-----
        if(sns && modDate == null || regDate == null){
                redirectStrategy.sendRedirect(request, response, "/balloon/my_info");
            }else if(sns && modDate.equals(regDate)){
                redirectStrategy.sendRedirect(request, response, "/balloon/my_info");
            }else{
            redirectStrategy.sendRedirect(request, response, "/balloon/main");
        }
    }

}
