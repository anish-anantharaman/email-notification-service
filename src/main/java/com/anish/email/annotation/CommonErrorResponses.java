package com.anish.email.annotation;

import com.anish.email.dto.ApiResponseDto;
import com.anish.email.util.swagger.Swagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(responseCode = "400",
                description = "Bad Request",
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
                        examples = @ExampleObject(value = Swagger.SwaggerExampleResponses.BAD_REQUEST)
                )),
        @ApiResponse(responseCode = "500",
                description = "Internal Server Error",
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
                        examples = @ExampleObject(value = Swagger.SwaggerExampleResponses.INTERNAL_SERVER_ERROR)
                ))
})
public @interface CommonErrorResponses {
}
