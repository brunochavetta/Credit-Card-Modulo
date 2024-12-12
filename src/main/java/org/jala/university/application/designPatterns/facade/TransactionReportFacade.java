package org.jala.university.application.designPatterns.facade;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.TransactionDto;
import org.jala.university.application.mapper.TransactionMapper;
import org.jala.university.application.service.CreditCardTransactionService;
import org.jala.university.domain.entity.CreditCardTransaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TransactionReportFacade {
    private final CreditCardTransactionService transactionService;

    public TransactionReportFacade() {
        this.transactionService = ServiceFactoryCreditCard.transactionServiceFactory();
    }

    public void generatePdfReport(List<CreditCardTransaction> transactions, String filePath)  throws FileNotFoundException, IOException{
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Transaction Report")
                    .setFontColor(ColorConstants.BLUE)
                    .setBold()
                    .setFontSize(18));

            Table table = new Table(4);
            table.addCell("Code");
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Amount");

            double totalAmount = 0;

            for (CreditCardTransaction transaction : transactions) {
                table.addCell(String.valueOf(transaction.getId()));
                table.addCell(transaction.getTransactionDate().toString());
                table.addCell(transaction.getDescription());
                table.addCell("$" + String.valueOf(transaction.getAmount()));
                totalAmount += transaction.getAmount();
            }

            table.addCell("");
            table.addCell("");
            table.addCell("Total:");
            table.addCell("$" + String.format("%.2f", totalAmount));

            document.add(table);
        }
    }

    public List<CreditCardTransaction> fetchTransactions(UUID creditCardId, LocalDate startDate, LocalDate endDate) {
        List<TransactionDto> transactionDtos = transactionService.findTransactionsByCardAndDateRange(creditCardId, startDate, endDate);
        return transactionDtos.stream().map(new TransactionMapper()::mapFrom).toList(); 
    }
}

