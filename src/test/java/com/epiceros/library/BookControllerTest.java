package com.epiceros.library;

import com.epiceros.library.controller.BookController;
import com.epiceros.library.dto.request.AddBookRequest;
import com.epiceros.library.dto.request.BorrowRequest;
import com.epiceros.library.dto.request.ReturnRequest;
import com.epiceros.library.dto.response.BooksByCategoryResponse;
import com.epiceros.library.dto.response.BorrowResponse;
import com.epiceros.library.dto.response.ReturnResponse;
import com.epiceros.library.entity.Author;
import com.epiceros.library.entity.Book;
import com.epiceros.library.entity.BookCategory;
import com.epiceros.library.exception.InvalidReturnRequestException;
import com.epiceros.library.service.BookService;
import com.epiceros.library.service.BorrowService;
import com.epiceros.library.service.ReturnService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BorrowService borrowService;

    @Mock
    private ReturnService returnService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void testBorrowBooksWithIDsMemberIDsAndBookIDs() throws Exception {
        Long memberId = 1L;
        List<Long> bookIds = Arrays.asList(101L, 102L, 103L);

        BorrowRequest request = new BorrowRequest(memberId, bookIds);;

        ResponseEntity<BorrowResponse> responseEntity = bookController.borrowBooks(request);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testReturnBooksWithLoanIDsANDBookIDs() throws Exception, InvalidReturnRequestException {
        List<Long> returnedBookIds = Arrays.asList(1L, 2L, 3L); // Example returned book IDs
        when(returnService.returnBooks(any())).thenReturn(returnedBookIds);

        List<Long> loanIds = Arrays.asList(1L, 2L, 3L);
        List<Long> bookIds = Arrays.asList(4L, 5L, 6L);

        ReturnRequest request = new ReturnRequest(loanIds, bookIds);

        ResponseEntity<ReturnResponse> responseEntity = bookController.returnBooks(request);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        ReturnResponse response = responseEntity.getBody();
        Assertions.assertNotNull(response.getReturnedBookIds());
        Assertions.assertEquals(returnedBookIds.size(), response.getReturnedBookIds().size());

    }

    @Test
    public void testAddingNewBook() throws Exception {
        Book mockBook = new Book();
        mockBook.setTitle("Test Book");
        mockBook.setCategory(BookCategory.NEW);
        mockBook.setPublicationDate(LocalDate.of(2023, 10, 15));
        mockBook.setCopiesOwned(5);

        doNothing().when(bookService).addBook(any(AddBookRequest.class));

        AddBookRequest bookRequest = new AddBookRequest();
        bookRequest.setBook(mockBook);

        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        bookRequest.setAuthors(Collections.singletonList(author));

        ResponseEntity<String> responseEntity = bookController.addBook(bookRequest);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetBooksByCategory() throws Exception {
        List<Book> mockBookList = new ArrayList<>();

        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Sample Book");
        mockBook.setCategory(BookCategory.NEW);
        mockBook.setPublicationDate(LocalDate.of(2022, 10, 15));
        mockBook.setCopiesOwned(5);

        mockBookList.add(mockBook);

        when(bookService.getBooksByCategory(anyString())).thenReturn(mockBookList);

        ResponseEntity<BooksByCategoryResponse> responseEntity = bookController.getBooksByCategory("NEW");

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        BooksByCategoryResponse response = responseEntity.getBody();
        Assertions.assertNotNull(response.getBooks());
        Assertions.assertEquals(mockBookList.size(), response.getBooks().size());
    }
}
