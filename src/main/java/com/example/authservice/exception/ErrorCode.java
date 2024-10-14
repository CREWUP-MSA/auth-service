package com.example.authservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.", 400),

    MEMBER_IS_DELETED("해당 회원은 탈퇴한 회원입니다.", 401),
    INVALID_REFRESH_TOKEN("리프레시 토큰이 유효하지 않습니다.", 401),

    MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.", 404),

    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다.", 500),
    ;

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
