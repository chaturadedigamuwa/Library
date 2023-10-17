package com.epiceros.library.service.impl;

import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.dto.response.MemberWithFine;
import com.epiceros.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Override
    public List<MemberWithFine> getMembersWithFines() {
       return memberDao.getMembersWithFines();
    }
}
