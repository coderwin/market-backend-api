package com.market.allra.web.dto;

import com.market.allra.domain.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 회원 정보
 */
@Getter
public class LoginMemberResponseDTO {
    private Long id;
    private String name;
    private String email;

    @Builder
    public LoginMemberResponseDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /* 비즈니스 로직 */

    public static LoginMemberResponseDTO create(Member member) {
        return LoginMemberResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
