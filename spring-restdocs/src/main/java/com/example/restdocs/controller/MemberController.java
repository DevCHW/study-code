package com.example.restdocs.controller;

import com.example.restdocs.controller.dto.request.MemberCreateRequestDto;
import com.example.restdocs.controller.dto.response.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members/{memberId}")
    public MemberResponseDto getMember(@PathVariable Long memberId) {
        return memberService.getMember(memberId);
    }

    @PostMapping("/api/v1/members")
    public MemberResponseDto createMember(
            @RequestBody MemberCreateRequestDto request
    ) {
        return memberService.createMember(request);
    }

}
