package org.jala.university.application.designPatterns.facade;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.layout.properties.TextAlignment;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardPayment;
import org.jala.university.domain.entity.CreditCardTransaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TransactionMonthlyReportFacade {
    public void generatePdfReport(List<CreditCardTransaction> transactions, String filePath)
            throws FileNotFoundException, IOException {

        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            CreditCardDebt debt = transactions.get(1).getDebt();

            int year = debt.getDueDate().getYear();
            String monthYear = debt.getMonth() + " " + year;
            document.add(new Paragraph("RESUME " + monthYear)
                    .setFontColor(ColorConstants.BLUE)
                    .setBold()
                    .setFontSize(18));

            Table transactionTable = new Table(4);
            transactionTable.addCell("Code");
            transactionTable.addCell("Date");
            transactionTable.addCell("Description");
            transactionTable.addCell("Amount");

            double totalAmount = 0;
            for (CreditCardTransaction transaction : transactions) {
                transactionTable.addCell(String.valueOf(transaction.getId()));
                transactionTable.addCell(transaction.getTransactionDate().toString());
                transactionTable.addCell(transaction.getDescription());
                transactionTable.addCell("$" + String.format("%.2f", transaction.getAmount()));
                totalAmount += transaction.getAmount();
            }

            transactionTable.addCell("");
            transactionTable.addCell("");

            Cell boldTitleTransactionCell = new Cell()
                    .add(new Paragraph("Total:"))
                    .setFont(PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY);
            transactionTable.addCell(boldTitleTransactionCell);

            Cell boldValueTransactionCell = new Cell()
                    .add(new Paragraph("$" + String.format("%.2f", totalAmount)))
                    .setFont(PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD))
                    .setTextAlignment(TextAlignment.RIGHT);
            transactionTable.addCell(boldValueTransactionCell);

            document.add(new Paragraph("Transaction Report")
                    .setBold()
                    .setFontSize(14));
            document.add(transactionTable);

            List<CreditCardPayment> payments = debt.getPayments();

            if (!payments.isEmpty()) {
                document.add(new Paragraph("\nPayments Made")
                        .setBold()
                        .setFontSize(14));

                Table paymentTable = new Table(2);
                paymentTable.addCell("Date");
                paymentTable.addCell("Amount");

                double totalPayments = 0;
                for (CreditCardPayment payment : payments) {
                    paymentTable.addCell(payment.getPaymentDate().toString());
                    paymentTable.addCell("$" + String.format("%.2f", payment.getAmount()));
                    totalPayments += payment.getAmount();
                }

                Cell boldTitlePaymentCell = new Cell()
                        .add(new Paragraph("Total Payments:"))
                        .setFont(PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD))
                        .setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                paymentTable.addCell(boldTitlePaymentCell);

                Cell boldValuePaymentCell = new Cell()
                        .add(new Paragraph("$" + String.format("%.2f", totalPayments)))
                        .setFont(PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD))
                        .setTextAlignment(TextAlignment.RIGHT);
                paymentTable.addCell(boldValuePaymentCell);

                document.add(paymentTable);

                double remainingDebt = debt.getOutstandingAmount();
                document.add(new Paragraph("\nTotal Due:")
                        .setFontColor(ColorConstants.RED)
                        .setBold()
                        .add("$" + String.format("%.2f", remainingDebt)));
            } else {
                document.add(new Paragraph("\nNo changes have been made to date.")
                        .setItalic()
                        .setFontSize(12));
            }
        }
    }

}
