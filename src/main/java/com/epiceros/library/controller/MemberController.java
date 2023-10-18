package com.epiceros.library.controller;

import com.epiceros.library.dto.response.MemberWithFine;
import com.epiceros.library.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members/v1")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

    @GetMapping("/members-with-fines")
    public List<MemberWithFine> getMembersWithFines() {
        logger.info("Received request to fetch members with fines.");
        return memberService.getMembersWithFines();
    }
}
