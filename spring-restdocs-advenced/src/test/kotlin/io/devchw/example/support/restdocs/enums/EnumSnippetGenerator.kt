package io.devchw.example.support.restdocs.enums

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.hhplus.board.support.restdocs.RestDocsTestSupport
import com.hhplus.board.support.restdocs.RestDocsUtils.requestPreprocessor
import com.hhplus.board.support.restdocs.RestDocsUtils.responsePreprocessor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType.*
import org.springframework.restdocs.payload.PayloadDocumentation.*

class EnumDocumentationTest : RestDocsTestSupport() {

    private lateinit var enumQueryService: EnumQueryService
    private lateinit var enumAdocGenerator: EnumAdocGenerator
    private lateinit var enumDocumentController: EnumDocumentController

    @BeforeEach
    fun setUp() {
        enumQueryService = EnumQueryService()
        enumAdocGenerator = EnumAdocGenerator()
        enumDocumentController = EnumDocumentController(enumQueryService, enumAdocGenerator)
        mockMvc = mockController(enumDocumentController)
    }

    @Test
    @DisplayName("enum 목록 문서화")
    fun enums() {
        val enumQuery = enumQueryService.getEnumQuery()

        // when
        given()
            .get("/api/v1/enums")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "enums/get-enums",
                    requestPreprocessor(),
                    responsePreprocessor(),
                    requestFields(
                        fieldWithPath("username").type(STRING).description("유저이름"),
                        fieldWithPath("password").type(STRING).description("비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("result").type(STRING).description("결과 타입 (SUCCESS / ERROR"),
                        fieldWithPath("data").type(OBJECT).description("데이터"),
                        fieldWithPath("data.accessToken").type(STRING).description("엑세스 토큰"),
                        fieldWithPath("data.refreshToken").type(STRING).description("리프레쉬 토큰"),
                        fieldWithPath("data.tokenType").type(STRING).description("토큰 타입(Bearer)"),
                        fieldWithPath("data.expiresIn").type(NUMBER).description("엑세스 토큰 만료 시간"),
                        fieldWithPath("data.message").type(STRING).description("메세지"),
                    ),
                ),
            )
    }
}