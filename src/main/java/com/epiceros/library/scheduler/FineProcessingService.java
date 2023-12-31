package com.epiceros.library.scheduler;

import com.epiceros.library.dao.FineDao;
import com.epiceros.library.dao.LoanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FineProcessingService {
    @Autowired
    private LoanDao loanDao;

    @Autowired
    private FineDao fineDao;
    public void processFines() {
        List<Long> overdueBookIds = loanDao.getOverdueBookIds();

        for (Long bookId : overdueBookIds) {
            double fineAmount = calculateFineForBook(bookId);
            fineDao.saveFine(bookId, fineAmount);
        }
    }
    private double calculateFineForBook(Long id) {
        // Logic to calculate fine goes here
        return 0;
    }
}
