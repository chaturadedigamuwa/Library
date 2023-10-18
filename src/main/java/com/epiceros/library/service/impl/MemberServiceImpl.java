package com.epiceros.library.service.impl;

import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.dto.response.MemberWithFine;
import com.epiceros.library.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MemberServiceImpl implements MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberDao memberDao;
    @Override
    public List<MemberWithFine> getMembersWithFines() {
        List<MemberWithFine> memberWithFines = memberDao.getMembersWithFines();
        logger.info("Found {} members with fines.", memberWithFines.size());
        return memberWithFines;
    }
}
