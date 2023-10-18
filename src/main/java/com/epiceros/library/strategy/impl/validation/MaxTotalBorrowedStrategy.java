package com.epiceros.library.strategy.impl.validation;

import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.entity.Member;
import com.epiceros.library.entity.MemberStatus;
import com.epiceros.library.exception.BorrowingLimitExceededException;
import com.epiceros.library.exception.BorrowingNotAllowedException;
import com.epiceros.library.strategy.CommonBorrowValidationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaxTotalBorrowedStrategy implements CommonBorrowValidationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MaxTotalBorrowedStrategy.class);

    private static final int MAX_TOTAL_BORROWED = 7;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private LoanDao loanDao;

    @Override
    public void validateBorrowing(Member member) {
        Long memberId = member.getId();
        MemberStatus status = memberDao.getMemberStatus(memberId);
        if (status == MemberStatus.INACTIVE) {
            logger.error("Inactive member with Member ID {}: {}", memberId);
            throw new BorrowingNotAllowedException("Member is inactive and not eligible to borrow");
        }
        if(loanDao.getLoanedBooksByMemberId(member.getId()).stream().count() > MAX_TOTAL_BORROWED) {
            logger.error("Exceeded maximum total borrowed articles", memberId);
            throw new BorrowingLimitExceededException("Exceeded maximum total borrowed articles");
        }
    }
}
