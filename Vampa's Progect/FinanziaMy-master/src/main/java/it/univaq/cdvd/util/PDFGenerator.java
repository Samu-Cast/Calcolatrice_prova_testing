package it.univaq.cdvd.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import it.univaq.cdvd.model.Transazione;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class PDFGenerator {

    public void generatePDF(List<Transazione> transazioni, String categoria, LocalDate dataInizio, LocalDate dataFine) {
        String fileName = "Report_" + categoria + "_" + dataInizio + "_to_" + dataFine + ".pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(fileName)));
            document.open();

            // Font personalizzati
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            // Titolo del documento
            Paragraph title = new Paragraph("Report delle Transazioni", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            // Sottotitoli con categoria e periodo
            Paragraph subTitle = new Paragraph("Categoria: " + categoria, subTitleFont);
            subTitle.setSpacingAfter(5);
            document.add(subTitle);

            Paragraph period = new Paragraph("Periodo: " + dataInizio + " - " + dataFine, subTitleFont);
            period.setSpacingAfter(15);
            document.add(period);

            // Aggiungere tabella per le transazioni
            PdfPTable table = new PdfPTable(3); // 3 colonne: data, descrizione, importo
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 6f, 2f}); // Larghezze relative delle colonne

            // Intestazioni della tabella
            table.addCell(new PdfPCell(new Phrase("Data", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Categoria", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Importo", tableHeaderFont)));

            // Contenuto della tabella
            for (Transazione transazione : transazioni) {
                table.addCell(new PdfPCell(new Phrase(transazione.getData().toString(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(transazione.getNomeCategoria(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", transazione.getImporto()), normalFont)));
            }

            document.add(table);

            // Messaggio di successo
            System.out.println("PDF generato: " + fileName);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
