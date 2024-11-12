package com.example.restdocs.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    private RestDocumentationContextProvider restDocumentation;
    protected MockMvcRequestSpecification mockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.restDocumentation = restDocumentation;
    }

    protected MockMvcRequestSpecification when() {
        return mockMvc;
    }

    // Mock Controller 구성
    protected MockMvcRequestSpecification mockController(Object controller) {
        RestAssured
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        RestAssuredMockMvcConfig config =
                RestAssuredMockMvcConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .defaultCharsetForContentType(StandardCharsets.UTF_8, ContentType.JSON))
                        .logConfig(LogConfig.logConfig()
                                .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                                .enablePrettyPrinting(true));

        return RestAssuredMockMvc.given()
                .config(config)
                .mockMvc(createMockMvc(controller))
                .contentType(ContentType.JSON).log()
                .everything(true);
    }

    // MockMvc 생성
    private MockMvc createMockMvc(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    // ObjectMapper 생성
    private ObjectMapper createObjectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(localDateTimeSerializer()); //LocalD

        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .registerModule(javaTimeModule);
    }

    // LocalDateTime 설정 (정해진 포멧이 따로 없다면 기본 포멧을 사용하면 됨)
    private LocalDateTimeSerializer localDateTimeSerializer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return new LocalDateTimeSerializer(formatter);
    }

}

