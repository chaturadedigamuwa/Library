package com.epiceros.library;

import com.epiceros.library.dao.BookDao;
import com.epiceros.library.dao.LoanDao;
import com.epiceros.library.dao.MemberDao;
import com.epiceros.library.dto.request.BorrowRequest;
import com.epiceros.library.entity.*;
import com.epiceros.library.exception.BorrowingLimitExceededException;
import com.epiceros.library.exception.MemberNotFoundException;
import com.epiceros.library.factory.BorrowingStrategyFactory;
import com.epiceros.library.service.BorrowService;
import com.epiceros.library.service.impl.BorrowServiceImpl;
import com.epiceros.library.strategy.impl.NewBookBorrowingStrategy;
import com.epiceros.library.strategy.impl.validation.MaxTotalBorrowedStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    @Mock
    private MemberDao memberDao;

    @Mock
    private BookDao bookDao;

    @Mock
    private LoanDao loanDao;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @InjectMocks
    private MaxTotalBorrowedStrategy maxTotalBorrowedStrategy;


    @Test(expected = IllegalArgumentException.class)
    public void testBorrowBooksExceedMaxBooksOfBorrowingTime() throws SQLException {
        BorrowRequest request = new BorrowRequest();
        request.setMemberId(1L);
        request.setBookIds(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L));  // Exceeding maximum allowed books

        borrowService.borrowBooks(request);
    }

    @Test(expected = BorrowingLimitExceededException.class)
    public void testValidateBorrowingExceedMaxTotalBorrowed() {
        final int MAX_TOTAL_BORROWED = 7;
        Member activeMember = new Member();
        activeMember.setId(1L);

        when(loanDao.getLoanedBooksByMemberId(anyLong())).thenReturn(createBookListWithSize(MAX_TOTAL_BORROWED + 1));

        maxTotalBorrowedStrategy.validateBorrowing(activeMember);
    }

    // Helper method to create a list of books with a specified size
    private List<Book> createBookListWithSize(int size) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            books.add(new Book());
        }
        return books;
    }

    // to be continued with other tests
}

