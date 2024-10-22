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
@ApiResponse(responseCode = "500", description = "서버에 문제가 발생했습니다.",
        content = @Content(
                schema = @Schema(implementation = CustomApiResponse.class),
                examples = {
                        @ExampleObject(
                                name = "서버에 문제가 발생했습니다.",
                                value = """
							{
								"message": "서버에 문제가 발생했습니다.",
							}
							"""
                        )
                }
        )
)
public @interface InternalServerErrorApiResponse {
}
