package com.market.allra.web.interceptor;

import com.market.allra.domain.Member;
import com.market.allra.repo.MemberRepository;
import com.market.allra.web.dto.LoginMemberResponseDTO;
import com.market.allra.web.dto.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 세션 내 자동 로그인 정보 저장 인터셉터
 */
@RequiredArgsConstructor
@Component
public class SessionInitInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if(session.getAttribute(SessionKeys.LOGIN_MEMBER) == null) {
            Long memberId = 1L;
            Member findMember = memberRepository.findById(memberId)
                    .orElse(null);

            // session 등록
            LoginMemberResponseDTO loginMemberDTO = LoginMemberResponseDTO.create(findMember);

            session.setAttribute(SessionKeys.LOGIN_MEMBER, loginMemberDTO);
        }

        return true;
    }
}
