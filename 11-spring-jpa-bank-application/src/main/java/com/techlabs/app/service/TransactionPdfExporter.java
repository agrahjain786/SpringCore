package com.techlabs.app.service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.exception.AccountException;

import jakarta.servlet.http.HttpServletResponse;

public class TransactionPdfExporter {
	
	private List<Transaction> transactions;

	public TransactionPdfExporter(List<Transaction> transactions) {
		super();
		this.transactions = transactions;
	}
	
	private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
         
        cell.setPhrase(new Phrase("Sender Account Number", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Receiver Account Number", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Transaction Type", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Transaction Date", font));
        table.addCell(cell);       
    }
     
    private void writeTableData(PdfPTable table) {
        for (Transaction transaction : transactions) {
            table.addCell(String.valueOf(transaction.getSenderAccountNumber()));
            table.addCell(String.valueOf(transaction.getReceiverAccountNumber()));
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getTransactionType());
            table.addCell(String.valueOf(transaction.getTransactionDate()));
        }
    }
     
    public void export(HttpServletResponse response){
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, response.getOutputStream());

			document.open();
			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setSize(18);
			font.setColor(Color.BLUE);

			Paragraph p = new Paragraph("List of Transactions", font);
			p.setAlignment(Paragraph.ALIGN_CENTER);

			document.add(p);

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100f);
			table.setWidths(new float[] { 2.5f, 2.5f, 2.0f, 2.5f, 3.0f });
			table.setSpacingBefore(10);

			writeTableHeader(table);
			writeTableData(table);

			document.add(table);

			document.close();
		}

		catch (DocumentException | IOException e) {
			throw new AccountException("Error while exporting pdf");
		}

	}

}
