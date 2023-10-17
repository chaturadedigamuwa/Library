package com.epiceros.library.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberWithFine {
    private Long memberId;
    private String firstName;
    private String lastName;
    private int totalFine;
}
