package com.epiceros.library.dao;

import com.epiceros.library.dto.response.MemberWithFine;
import com.epiceros.library.entity.Member;
import com.epiceros.library.entity.MemberStatus;

import java.util.List;

public interface MemberDao {
    Member getMemberById(Long memberId);
    MemberStatus getMemberStatus(Long memberId);

    List<MemberWithFine> getMembersWithFines();

}
