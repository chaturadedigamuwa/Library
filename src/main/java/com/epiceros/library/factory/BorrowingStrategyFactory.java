package com.epiceros.library.factory;

import com.epiceros.library.entity.BookCategory;
import com.epiceros.library.strategy.BorrowingStrategy;
import com.epiceros.library.strategy.impl.ClassicBookBorrowingStrategy;
import com.epiceros.library.strategy.impl.NewBookBorrowingStrategy;
import com.epiceros.library.strategy.impl.StandardBookBorrowingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowingStrategyFactory {

    @Autowired
    private NewBookBorrowingStrategy newBookBorrowingStrategy;
    @Autowired
    private ClassicBookBorrowingStrategy classicBookBorrowingStrategy;
    @Autowired
    private StandardBookBorrowingStrategy standardBookBorrowingStrategy;

    public BorrowingStrategy getStrategy(BookCategory category) {
        switch (category) {
            case NEW:
                return newBookBorrowingStrategy;
            case CLASSIC:
                return classicBookBorrowingStrategy;
            case STANDARD:
                return standardBookBorrowingStrategy;
            default:
                throw new IllegalArgumentException("Invalid book category.");
        }
    }
}

