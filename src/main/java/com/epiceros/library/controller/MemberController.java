package com.epiceros.library.controller;

import com.epiceros.library.dto.response.MemberWithFine;
import com.epiceros.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members/v1")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/members-with-fines")
    public List<MemberWithFine> getMembersWithFines() {
        return memberService.getMembersWithFines();
    }
}
