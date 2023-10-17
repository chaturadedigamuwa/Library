package com.epiceros.library.service;

import com.epiceros.library.dto.response.MemberWithFine;

import java.util.List;

public interface MemberService {

    List<MemberWithFine> getMembersWithFines();
}
