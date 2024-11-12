백엔드 개발을 하다보면 API 명세서를 작성해야 할 일이 많습니다.

API 명세서를 작성하는 방법이야 여러가지가 있지만, 저는 개인적으로 사이드프로젝트나 업무에서 Spring의 Restdocs를 이용해서 작성하는걸 선호하는데요, 테스트를 기반으로 문서가 작성되기 때문에 개발 중간에 API 스펙이 변경되면 테스트가 통과하지 못해서 강제로(?) 문서 테스트도 수정해야하기 때문에 실제 API 스펙과 문서에 명시되어 있는 스펙이 달라질 일이 없는 점과, 프로덕션 코드에는 문서 작성을 위한 코드가 전혀 들어가지가 않기에 비교적 깔끔한 프로덕션 코드를 유지할 수 있기 때문입니다.

문제는 RestDocs를 처음 적용하려고 할 때 설정이 조금 복잡하고 작성법이 생소하기 때문에, 실험적으로 작성하시는 분들께는 꽤나 버거울 수도 있겠다는 생각에, 오늘은 예제 코드를 통해서 Spring RestDocs 사용을 해보고 장점에 대해서 체감해보는 포스팅을 진행하겠습니다.

예제에 사용된 모든 코드는 [https://github.com/DevCHW/study-code/tree/main/spring-restdocs](https://github.com/DevCHW/study-code/tree/main/spring-restdocs) 에서 확인하실 수 있습니다.

### 예제 코드 작성을 위한 환경 설정

먼저 예제 코드 작성을 위하여 Spring boot 3.3.5에 Java 17버전을 사용하여 프로젝트를 생성하겠습니다. 혹시 다른 버전일 경우 문법이 살짝 다를 수 있기 때문에 참고해주세요 !

**build.gradle**

```
dependencies {
    // web
    implementation 'org.springframework.boot:spring-boot-starter-web'

    // lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // test
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

디펜던시는 기본적으로 spring-starter-web과, lombok, test를 추가해주었습니다. 나머지 기본 설정들은 생략하도록 하겠습니다.

![image](https://blog.kakaocdn.net/dn/y0lJx/btsKGPTo11L/ng618b0fAycuT0CPkEkYr1/img.png)

그 뒤, 문서로 작성할 회원 목록 조회 API와 회원 생성 API를 만들어주기 위하여 위의 파일들을 생성합니다.

**MemberResponseDto**

```
package com.example.restdocs.controller.dto.response;

public record MemberResponseDto(
        Long id,
        String name,
        int age
) {
}
```

**MemberCreateRequestDto**

```
package com.example.restdocs.controller.dto.request;

public record MemberCreateRequestDto(
        String name,
        int age
) {
}
```

**MemberController**

```
package com.example.restdocs.controller;

import com.example.restdocs.controller.dto.request.MemberCreateRequestDto;
import com.example.restdocs.controller.dto.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members/{memberId}")
    public MemberResponseDto getMember(
    	@PathVariable("memberId") Long memberId
    ) {
        return memberService.getMembers(memberId);
    }

    @PostMapping("/api/v1/members")
    public MemberResponseDto createMember(
            @RequestBody MemberCreateRequestDto request
    ) {
        return memberService.createMember(request);
    }

}
```

**MemberService**

```
package com.example.restdocs.controller;

import com.example.restdocs.controller.dto.request.MemberCreateRequestDto;
import com.example.restdocs.controller.dto.response.MemberResponseDto;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public MemberResponseDto getMember(Long memberId) {
        // TODO("Not yet implemented")
        return null;
    }

    public MemberResponseDto createMember(MemberCreateRequestDto request) {
        // TODO("Not yet implemented")
        return null;
    }
}
```

자세한 코드 설명은 생략하고 상황을 설명하자면, 아직 MemberService의 \`getMember()\`와 \`createMember()\` 메소드는 구현이 되어있지 않은 상황이고 API의 엔드포인트, Request, Response 스펙만 간단하게 Controller에 정의해둔 상황입니다.

실제로 저는 업무를 하며 API 명세를 전달할 때 위 처럼 API 스펙 껍데기를 먼저 구현해둔 뒤, 프론트엔드 개발자분과 합의가 끝나면 뒷단의 구현부를 채워나가는 식으로 개발을 진행하곤 합니다. 그래서 최대한 실무와 비슷한 상황을 가정하였습니다 !

여기까지 따라오셨다면 이제 위 두개의 API의 스펙을 RestDocs를 이용하여 작성해보도록 하겠습니다.

### Rest Docs 설정

먼저 Rest Docs를 사용하기 위해서 build.gradle에 아래의 설정들을 추가해줍니다.

**asciidoctor 플러그인 추가**

```
plugins {
    id 'org.asciidoctor.jvm.convert' version "3.3.2"
}
```

**스니펫 경로 변수 설정 및 asciiidoctor를 확정하는 asciidoctorExt에 대한 종속성 구성 선언**

```
ext {
    snippetsDir = file('build/generated-snippets') // 스니펫 경로 변수 설정
}

configurations {
    asciidoctorExt
}
```

**restdocs 관련 디펜던시 추가** (참고로 저는 restassured를 이용하여 작성하였습니다. restassured 없이 mockmvc만으로도 작성할 수 있습니다.)

```
dependencies {
    // restdocs
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    testImplementation 'org.springframework.restdocs:spring-restdocs-restassured'
    testImplementation 'io.rest-assured:spring-mock-mvc'
}
```

test task에 output 경로로 스니펫 경로 설정

```
tasks.named('test') {
    outputs.dir snippetsDir
    useJUnitPlatform()
}
```

**asciidoctor task 설정**

```
asciidoctor {
    dependsOn test
    configurations 'asciidoctorExt'
    baseDirFollowsSourceFile()	// 다른 adoc 파일을 include 할 때 경로를 baseDir로 맞춘다.
    inputs.dir snippetsDir
    outputDir file('build/docs/asciidoc')

    sources {
        include("**/index.adoc") // html로 만들 adoc 파일 설정. 추가로 더 지정할 수 있음
    }
}
```

resolveMainClassName 작업이 copyDocument 작업 이후에 수행되도록 명시

```
tasks.resolveMainClassName {
    dependsOn 'copyDocument'
}
```

jar 작업이 copyDocument 작업 이후에 수행되도록 명시

```
tasks.jar {
    dependsOn 'copyDocument'
}
```

**copyDocument Task 추가**

```
// Task 추가
tasks.register('copyDocument', Copy) {
    from file("build/docs/asciidoc")
    into file("build/resources/main/static/docs") // 필요에 따라 static 대신 templates로 변경

    include "index.html"

    rename "index.html", "api-docs.html"

    dependsOn asciidoctor
}
```

**build, bootJar 테스크에 copyDocument 이후 수행 되도록 설정**

```
build {
    dependsOn copyDocument
}

bootJar {
    dependsOn copyDocument
}
```

**위 설정을 추가한 전체 build.gradle** (관련 없는 설정 부분은 제외하였습니다.)

```
plugins {
    id 'org.asciidoctor.jvm.convert' version "3.3.2"
}

// 전역 변수 선언
ext {
    snippetsDir = file('build/generated-snippets')
}

configurations {
    asciidoctorExt
}

dependencies {
    // .. 기타 의존성

    // restdocs
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    testImplementation 'org.springframework.restdocs:spring-restdocs-restassured'
    testImplementation 'io.rest-assured:spring-mock-mvc'
}

tasks.named('test') {
    outputs.dir snippetsDir
    useJUnitPlatform()
}


asciidoctor {
    dependsOn test
    configurations 'asciidoctorExt'
    baseDirFollowsSourceFile()	// 다른 adoc 파일을 include 할 때 경로를 baseDir로 맞춘다.
    inputs.dir snippetsDir
    outputDir file('build/docs/asciidoc')

    sources {
        include("**/index.adoc") // html로 만들 adoc 파일 설정
    }
}

tasks.resolveMainClassName {
    dependsOn 'copyDocument'
}

// Task 추가
tasks.register('copyDocument', Copy) {
    from file("build/docs/asciidoc")
    into file("build/resources/main/static/docs")

    include "index.html"

    rename "index.html", "api-docs.html"

    dependsOn asciidoctor
}


build {
    dependsOn copyDocument
}

bootJar {
    dependsOn copyDocument
}
```

다음, src/docs/asciidoc 경로에 index.adoc 파일을 추가하고 기본적인 설정을 채워줍니다.

![](https://blog.kakaocdn.net/dn/bAnqfO/btsKHbIBiWE/Zq8Ce5JfqzThom5OgSxaa1/img.png)

**index.adoc**

```
ifndef::snippets[]
:snippets: build/generated-snippets
endif::[]
= REST API 문서
:hardbreaks:
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 2
:sectlinks:

== 회원 API

=== 회원 생성
회원을 생성하는 API 입니다.

==== Request HTTP Example
include::{snippets}/create-member/http-request.adoc[]
==== Request Fields
include::{snippets}/create-member/request-fields.adoc[]
==== Response HTTP Example
include::{snippets}/create-member/http-response.adoc[]
==== Response Fields
include::{snippets}/create-member/response-fields.adoc[]

=== 회원 단건 조회
회원 정보를 조회하는 API 입니다.

==== Request HTTP Example
include::{snippets}/get-members/http-request.adoc[]
==== Path Parameters
include::{snippets}/get-members/path-parameters.adoc[]
==== Response HTTP Example
include::{snippets}/get-members/http-response.adoc[]
==== Response Fields
include::{snippets}/get-members/response-fields.adoc[]
```

![](https://blog.kakaocdn.net/dn/cw6bgg/btsKHxEEJLu/kCCjCuFcRxTNDa1rbIimg0/img.png)

여기까지 따라오셨다면, 위와 같은 화면을 보실 수 있을 겁니다.

오른쪽에 알 수 없는 말과 함께 뭔가 비정상적인(?) 화면이 렌더링 되는것은 아직 테스트를 통한 문서 스니펫을 생성되지 않았기 때문에 스니펫을 찾을 수 없다는 문구입니다. 정상적으로 동작하고 있는 것이니 안심하셔도 됩니다. :)

자, 그 다음은 제가 기본적으로 RestDocs 사용 시 추가해주는 문서 작성 유틸 클래스 두 개를 추가해줍니다.

![](https://blog.kakaocdn.net/dn/bAnqfO/btsKHbIBiWE/Zq8Ce5JfqzThom5OgSxaa1/img.png)

**RestDocsTestSupport**

```
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

        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .registerModule(javaTimeModule);
    }

}
```

**RestDocsTestUtils**

```
package com.example.restdocs.support;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

public class RestDocsUtils {

    public static OperationRequestPreprocessor requestPreprocessor() {
        return Preprocessors.preprocessRequest(
                Preprocessors.modifyUris()
                        .scheme("https")
                        .host("example.restdocs.com")
                        .removePort(),
                Preprocessors.prettyPrint());
    }

    public static OperationResponsePreprocessor responsePreprocessor() {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
    }

}
```

스프링에서 전체 테스트를 수행해보신 분들은 아시겠지만, 테스트 수행마다 매번 컨테이너를 띄우게 되면 수행 속도가 굉장히 오래걸립니다.

그래서 위의 RestDocsTestSupport 클래스처럼 추상 클래스를 만들고, 하위에서 상속받아 테스트를 수행하면 상속받은 클래스들은 부모의 컨테이너를 공유하기 때문에 수행 속도 측면에서 이점이 있어 위 처럼 구성하였습니다.

여기까지 따라오셨다면 RestDocs 환경 설정은 끝입니다! 이제 만들어둔 API의 문서를 작성해보겠습니다.

### Rest Docs로 만들어둔 API의 문서 작성하기

```
class MemberControllerTest extends RestDocsTestSupport { // (1)
    
    private MemberService memberService;
    private MemberController memberController;
    
    @BeforeEach // (2)
    void setUp() {
        memberService = mock(MemberService.class); // (3)
        memberController = new MemberController(memberService); // (4)
        mockMvc = mockController(memberController); // (5)
    }

}
```

(1) - 테스트 디렉토리에 MemberControllerTest 클래스를 만들고, 만들어둔 RestDocsTestSupport를 상속받습니다.

(2) - @BeforeEach 어노테이션을 통해 각각의 테스트를 수행하기 전 수행되도록 구성합니다.

(3) - MemberService의 메서드 들은 아직 구현이 안되어있습니다. Mockito 프레임워크의 mock() 함수를 통해 MemberService의 Mock 객체를 만들어줍니다.

(4) - MemberController를 (3)에서 만든 MemberService의 Mock 객체를 생성자에 넣어 생성합니다.

(5) - RestDocsController에서 만든 mockController() 함수에 memberController를 인자로 넘겨주어 mockMvc를 만들어줍니다. mockController() 메서드는 접근제한자가 protected였기때문에 자식 클래스에서 바로 호출할 수 있습니다. 

**회원 단건 조회 API 문서 테스트**

```
@DisplayName("회원 단건 조회 API 문서 테스트")
@Test
void getMember() {
    Long memberId = 1L;
    MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "최현우", 29);

    // Service에서 리턴되는 값을 미리 정의. (Stubbing)
    given(memberService.getMember(anyLong()))
            .willReturn(memberResponseDto);

    when().get("/api/v1/members/{memberId}", String.valueOf(memberId)) // 엔드포인트, Path Variable 형식으로 작성. Path Variable이 없을 경우 생략 가능
            .then().log().all() // HTTP Request / Response 로깅
            .statusCode(HttpStatus.OK.value()) // API 문서를 만들기 위한 테스트이므로 HTTP Status Code가 200인 것만 검증.
            .apply(document(
                    "get-member", // API 이름
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
```

저는 문서의 가독성을 위해서, 대부분을 static import 해둔 상태이기 때문에 주의하셔서 작성해야 합니다. 작성하실 경우 하단에 전체 코드를 보고 하나하나 주의해서 import 하시기 바랍니다.

또한 저는 BDDMockito.given() 메서드를 이용하여 MemberService의 Mock 객체에 대한 행동을 미리 정의해두었는데요, 이번 포스팅 범위에서 BDDMockito나 Mockito를 이용하여 Stubbing 하는것을 설명하기엔 범위에 벗어나서 자세한 설명은 생략하도록 하겠습니다.

잘 모르시는분들 께서는 대충 이렇게 아직 실제로 구현이 안된 Service의 로직을 미리 정의할 수 있구나 정도로 생각해주시면 됩니다.= 

위 처럼 테스트를 작성하고 수행한다면?

![](https://blog.kakaocdn.net/dn/c5jW2B/btsKGTBqPAM/r84LItTBDw7LkBnQyF8cwk/img.png)

![](https://blog.kakaocdn.net/dn/dOpprQ/btsKFMJ3U6F/lgRkxBTV51ZzHCEYhwTfY0/img.png)

테스트가 통과하고 API 이름 폴더 하위에 스니펫들이 생성된 것을 볼 수 있습니다.

![](https://blog.kakaocdn.net/dn/edsu3F/btsKHbIB7tz/kzx9P3o3AKj8zKBNK9E08k/img.png)

다시 아까 작성해 둔 index.adoc 파일을 열어 확인해보면, 이제는 스니펫이 있기 때문에 정상적으로 스니펫을 불러와서 화면이 렌더링 된 것을 확인할 수 있습니다.

아직 회원 생성에 대한 문서 작성은 하지 않았기 때문에,  회원 생성에 대한 문서 작성도 마저 해보겠습니다.

![](https://blog.kakaocdn.net/dn/bI38gy/btsKGYbFxBM/Qf3yTziTkPLJLxz7PC3Rtk/img.png)

회원 생성 API는 회원 단건 조회 API와 다르게 HTTP Post 메서드를 사용하기 때문에, Request Body 값을 넣어주고, Request Body 필드들에 대한 값 설명을 명시해주었습니다.

### API 문서 배포하기

Asciidoc파일에 스니펫을 통해서 작성된 것은 알겠는데, 이제 이 문서를 프론트엔드 개발자분께 전달해드려야겠죠?

이미 위의 build.gradle 설정에서 jar로 패키징하기 전 Asciidoc 파일을 HTML로 변환하여 resources 폴더로 옮겨지도록 설정해두었기 때문에, 이건 굉장히 쉽습니다.

여러분들의 스크롤은 소중하니까(?) 다시 build.gradle 설정 일부분을 보겠습니다.

```
asciidoctor { // (1)
    dependsOn test
    configurations 'asciidoctorExt'
    baseDirFollowsSourceFile()  // 다른 adoc 파일을 include 할 때 경로를 baseDir로 맞춘다.
    inputs.dir snippetsDir
    outputDir file('build/docs/asciidoc')

    sources {
        include("**/index.adoc") // html로 만들 adoc 파일 설정
    }
}

tasks.resolveMainClassName {
    dependsOn 'copyDocument'
}

// Task 추가
tasks.register('copyDocument', Copy) { // (2)
    from file("build/docs/asciidoc")
    into file("build/resources/main/static/docs")

    include "index.html"

    rename "index.html", "api-docs.html"

    dependsOn asciidoctor
}

build { // (3)
    dependsOn copyDocument
}
```

(1) - 테스트가 수행되고 asciidoctor 테스크가 수행됩니다. 간단하게 스니펫 경로를 세팅해주고 index.adoc 파일만들 HTML 파일로 만들도록 세팅해주었습니다.

(2) - asciidoctor 테스크가 수행되고, copyDocument가 수행됩니다. build/docs/asciidoc 경로에 있는 index.html 파일을 build/resources/main/static/docs 경로로 카피한 뒤, 이름을 api-docs.html로 변경합니다.

(3) - copyDocument 테스크가 수행된 이후, build 테스크가 수행됩니다.

정말 위 시나리오대로 수행되는지 로컬에서 눈으로 확인해보기 위하여 인텔리제이에서 build를 수행해보겠습니다.

![](https://blog.kakaocdn.net/dn/dnNA4W/btsKG8SFyP6/1wyMtBjakxqKS6eK6wJns0/img.png)

![](https://blog.kakaocdn.net/dn/0iVtZ/btsKFfzbE5q/IY1PWahlvbBKzVnUOiyzUk/img.png)

설정한 대로 build가 수행되기 전 test -> asciidoctor -> copyDocument 순으로 수행되었음을 확인할 수 있습니다.

![](https://blog.kakaocdn.net/dn/Y5OpH/btsKHvfLZ6F/xOCA2Y92FPJOunssn8qC2k/img.png)

또한 생성된 build 디렉토리에서 설정해둔 경로 하위에 api-docs.html 파일이 생성된 것을 확인할 수 있는데요, 해당 HTML 파일을 열어서 확인해보면?

![](https://blog.kakaocdn.net/dn/bF4TdO/btsKF5WUR3m/XhwVsve3SY1vqNSbRCXP41/img.png)

위와 같이 API 문서가 잘 나오는 것을 확인해볼 수 있습니다!

빌드 시 자동으로 정적 리소스에 포함되어 jar 파일로 패키징 되기 때문에, 개발용 서버에 배포 이후 프론트엔드 개발자분께는

https://{도메인}/{HTML 파일 경로} URL을 드리면 쉽게 확인하실 수 있습니다.

### RestDocs 장점 체감하기

이제 문서 작성은 모두 끝났지만, 다른 라이브러리에 비해서 설정해줘야 하는게 너무 많고, 문서를 위해 작성해야하는 코드 양이 상당히 많다 보니 대체 이게 뭐가 좋은지 체감이 잘 안되시는 분들이 있을 겁니다. (제가 그랬거든요.)

그런데 한 번 이렇게 API 문서를 작성해두면, 이후 개발 도중 API 스펙에 대한 수정 사항에 대해서 문서 또한 실제 API 스펙과 맞춰주어야만 테스트가 성공하기 때문에 **문서 상 API 스펙과 실제 API 스펙이 달라질 일을 사전에 방지**해줍니다.

잘 와닿지 않는다면 프론트엔드 개발자분이 API 스펙 변경 요청을 했다고 가정해보겠습니다.

> 프론트엔드 개발자 : 혹시 회원 단건 조회 API에서 나이를 숫자 타입이 아닌 문자 타입으로 넘겨주실 수 있나요? 그리고 프로필이미지 URL도 필요할 것 같아요. 응답 스펙에 추가해주세요.

요구사항을 만족하기 위하여, 만들어둔 Response DTO에 타입 변경 및 필드를 추가해줍니다.

**MemberResponseDto**

```
package com.example.restdocs.controller.dto.response;

public record MemberResponseDto(
        Long id,
        String name,
        String age, // 타입 변경
        String profileImageUrl // 필드 추가
) {
}
```

![](https://blog.kakaocdn.net/dn/cf719F/btsKGzKimKm/dVvosbD0j8ERxHgd7kfno1/img.png)

1차적으로 테스트 코드에서 예시를 위해 생성해둔 DTO 생성자에서 컴파일 에러가 발생합니다.

![](https://blog.kakaocdn.net/dn/BPjl2/btsKFefZmtd/bQGKY7pF0c0w9ZSMeAD5B0/img.png)

대충 위 사진처럼 컴파일 에러는 잡고, 테스트를 다시 돌려봅시다.

![](https://blog.kakaocdn.net/dn/bJQ4EV/btsKGQ5Nskd/OVkmyPn0YksSi0ZyVgY500/img.png)

profileImageUrl 필드를 찾을 수 없다며 테스트가 실패하는 것을 확인할 수 있습니다.

![](https://blog.kakaocdn.net/dn/dF78dp/btsKHIMJ0tF/f89VZLttAD6Jgy63OzyxAk/img.png)

그래서 위 사진처럼 profileImageUrl 필드에 대한 문서 코드를 작성하고 다시 테스트를 하면?

![](https://blog.kakaocdn.net/dn/EKevf/btsKGP6Sc1m/F41OofSum5VGLxwsKTEWC0/img.png)

이번에는 'age' 필드가 Number로 작성되었지만 실제로는 String 타입이라며 테스트가 실패합니다.

![](https://blog.kakaocdn.net/dn/dvAqK0/btsKHpNps5C/SXPetvU3oYdjSM75W1q7y1/img.png)

다시 실제 API 스펙에 맞춰 age 필드의 타입을 STRING으로 변경해준 뒤 테스트를 해주면?

![](https://blog.kakaocdn.net/dn/cYuAlR/btsKGFKepWt/2AAFqi9oqdC7KTDOMKtKeK/img.png)

비로소 테스트가 성공하며 문서 스니펫이 만들어지는 것을 확인할 수 있습니다 !

테스트가 성공해야 빌드가 되기 때문에, 위 상황처럼 문서에 타입을 잘못 표기하거나, 추가된 필드를 누락할 일이 없어 문서에 대한 신뢰성을 보장할 수 있게 됩니다.

### 마치며..

빠르게 일을 쳐내야 하는 실무 상황 속에서 문서 작성을 수동으로 하는 경우 개발자의 실수로 API만 수정하고 문서 수정은 하지않아서 결국 문서로써의 가치를 잃게 되는 경우를 종종 보았는데요. (~경험담~)

개인적으로 API 문서는 무엇보다 "신뢰성"이 가장 중요하다고 생각합니다. 신뢰성이 보장되지 않는 API 문서를 가지고 협업을 한다면 그것은 결국 장애로 이어질 테니까요.

이러한 측면에서 RestDocs는 스프링 프레임워크로 개발하시는 분들 께 좋은 선택지가 될 것 같습니다.

물론 단점도 있는데요,

-   문서를 위해서 작성해야하는 테스트코드가 너무 많다. (숙련도에 따라서 작성에 굉장히 많은 시간이 걸릴 수 있음)
-   초기 설정을 이해하고 적용하는 데 러닝커브가 있다. (팀 전체에 적용을 하려면 구성원들 모두가 이를 이해하고 있어야겠죠?)
-   빠르게 프로덕션 코드만 수정하고 개발 서버에 배포해보고 싶은데, 문서도 강제로 작성을 해야한다.
-   못생긴 UI...

또한 한계점도 존재합니다. 각 요청/응답 필드에 대한 Description은 제대로 썼는지에 대해 검증할 방법이 없어 이 부분은 실수할 여지가 있고, Enum 타입의 경우 Description에 하나하나 값들을 수동으로 명시해주어야 합니다.

또한 Optional한 값에 대한 명시를 문서에 녹이려면 스니펫을 커스텀해주어야 하는데요, 다음 포스팅에서는 커스텀 스니펫을 구성하는 방법과 Enum 타입에 대해서 Description 작성 자동화를 하는 방법에 대해 알아보겠습니다.

긴 글 읽어주셔서 감사합니다. :)