package com.anish.email.annotation;

import com.anish.email.dto.ApiResponseDto;
import com.anish.email.util.swagger.Swagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "202",
        description = "Notification queued successfully",
        headers = {
                @io.swagger.v3.oas.annotations.headers.Header(
                        name = "requestId",
                        description = "Unique ID for tracking this request",
                        schema = @Schema(type = "string", example = "c828bdd1-2fa6-4d58-8d25-8bb7fa9cdc1f")
                )
        },
        content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiResponseDto.class),
                examples = @ExampleObject(value = Swagger.SwaggerExampleResponses.ACCEPTED)
        ))
public @interface AcceptedResponse {
}