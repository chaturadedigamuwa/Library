package com.epiceros.library.dto.request;

import java.util.List;

public class BorrowRequest {
    private Long memberId;
    private List<Long> bookIds;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }

}


