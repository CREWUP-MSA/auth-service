package com.example.authservice.config.swagger;

import com.example.authservice.dto.CustomApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "401", description = "해당 회원은 탈퇴한 회원입니다.",
        content = @Content(
                schema = @Schema(implementation = CustomApiResponse.class),
                examples = {
                        @ExampleObject(
                                name = "해당 회원은 탈퇴한 회원입니다.",
                                value = """
							{
								"message": "해당 회원은 탈퇴한 회원입니다.",
							}
							"""
                        )
                }
        )
)
public @interface MemberIsDeletedApiResponse {
}
