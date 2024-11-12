package com.example.restdocs.controller;

import com.example.restdocs.controller.dto.request.MemberCreateRequestDto;
import com.example.restdocs.controller.dto.response.MemberResponseDto;
import com.example.restdocs.support.RestDocsTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.example.restdocs.support.RestDocsUtils.requestPreprocessor;
import static com.example.restdocs.support.RestDocsUtils.responsePreprocessor;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

class MemberControllerTest extends RestDocsTestSupport {

    private MemberService memberService;
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        memberService = mock(MemberService.class);
        memberController = new MemberController(memberService);
        mockMvc = mockController(memberController);
    }

    @DisplayName("회원 단건 조회 API 문서 테스트")
    @Test
    void getMember() {
        Long memberId = 1L;
        MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "최현우", 29);

        // Service에서 리턴되는 값을 미리 정의. (Stubbing)
        given(memberService.getMember(anyLong()))
                .willReturn(memberResponseDto);

        when().get("/api/v1/members/{memberId}", String.valueOf(memberId)) // 엔드포인트, path
                .then().log().all() // HTTP Request / Response 로깅
                .statusCode(HttpStatus.OK.value()) // API 문서를 만들기 위한 테스트이므로 HTTP Status Code가 200인 것만 검증.
                .apply(document(
                        "get-member", // API 이름 작성
                        requestPreprocessor(),
                        responsePreprocessor(),
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields( // 응답 필드 문서 작성
                                fieldWithPath("id").type(NUMBER).description("회원 ID"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("age").type(NUMBER).description("나이")
                        ))
                );
    }

    @DisplayName("회원 생성 API 문서 테스트")
    @Test
    void createMember() {
        MemberCreateRequestDto request = new MemberCreateRequestDto("최현우", 29);
        MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "최현우", 29);

        // Service에서 리턴되는 값을 미리 정의. (Stubbing)
        given(memberService.createMember(any(MemberCreateRequestDto.class)))
                .willReturn(memberResponseDto);

        when().body(request)
                .post("/api/v1/members")
                .then().log().all() // HTTP Request / Response 로깅
                .statusCode(HttpStatus.OK.value()) // API 문서를 만들기 위한 테스트이므로 HTTP Status Code가 200인 것만 검증.
                .apply(document(
                        "create-member", // API 이름 작성
                        requestPreprocessor(),
                        responsePreprocessor(),
                        requestFields( // 요청 필드 문서 작성
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("age").type(NUMBER).description("나이")
                        ),
                        responseFields( // 응답 필드 문서 작성
                                fieldWithPath("id").type(NUMBER).description("회원 ID"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("age").type(NUMBER).description("나이")
                        ))
                );
    }

}
