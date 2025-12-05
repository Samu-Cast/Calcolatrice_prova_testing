package it.univaq.cdvd.testLogicaBusiness;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.PDFGenerator;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class PDFGeneratorTest {


    @BeforeAll
    public void setup() throws IOException {
        HibernateUtil.setDbms("/hibernate-test.cfg.xml");
    }


    @Test
    void testGeneratePDF() {
        // Arrange
        PDFGenerator generator = new PDFGenerator();
        List<Transazione> transazioni = new ArrayList<>();
        String categoria = "TestCategoria";
        LocalDate dataInizio = LocalDate.of(2023, 1, 1);
        LocalDate dataFine = LocalDate.of(2023, 1, 31);
        String fileName = "Report_" + categoria + "_" + dataInizio + "_to_" + dataFine + ".pdf";

        // Act
        generator.generatePDF(transazioni, categoria, dataInizio, dataFine);

        // Assert
        File pdfFile = new File(fileName);
        assertTrue(pdfFile.exists(), "Il file PDF non è stato generato.");
    }
}

