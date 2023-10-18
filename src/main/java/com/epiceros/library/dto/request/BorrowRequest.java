package com.epiceros.library.dto.request;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class BorrowRequest {
    private Long memberId;
    private List<Long> bookIds;

    public BorrowRequest(Long memberId, List<Long> bookIds) {
        this.memberId = memberId;
        this.bookIds = bookIds;
    }

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


