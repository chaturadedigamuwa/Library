package com.epiceros.library.dao;

import com.epiceros.library.entity.Member;

public interface MemberDao {
    Member getMemberById(Long memberId);
}
