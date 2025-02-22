package com.nutrehogar.sistemacontable.application.report;

import com.nutrehogar.sistemacontable.application.MainClass;
import com.nutrehogar.sistemacontable.application.dto.JournalEntryDTO;
import com.nutrehogar.sistemacontable.application.dto.LedgerRecordDTO;
import com.nutrehogar.sistemacontable.domain.model.User;
import com.nutrehogar.sistemacontable.exception.ReportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class ReportControllerTest {
    ReportController reportController;
    @BeforeEach
    void setUp() {
        MainClass.USER = User.builder().password("123").username("Yp").isEnable(true).build();
        reportController = new ReportController();
    }
    @Test
    void generateReport() throws ReportException {
        var ledjer = List.of(new LedgerRecordDTO("762","9378","uy","uwjh","ush","usgu"));
        var dto = new JournalEntryDTO(87278, "este", LocalDate.now(), "Este","concep", "1863", ledjer);
        reportController.generateReport(PaymentVoucher.class, dto);
        reportController.generateReport(RegistrationForm.class, dto);
    }
}