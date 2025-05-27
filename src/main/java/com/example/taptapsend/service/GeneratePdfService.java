package com.example.taptapsend.service;
 
import com.example.taptapsend.model.Client;
import com.example.taptapsend.model.Envoyer;
import com.example.taptapsend.repository.ClientRepository;
import com.example.taptapsend.repository.EnvoyerRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GeneratePdfService {

    private final ClientRepository clientRepository;
    private final EnvoyerRepository envoyerRepository;
    private final GetDevise getDevise;

    public GeneratePdfService(ClientRepository clientRepository,EnvoyerRepository envoyerRepository,GetDevise getDevise){
        this.clientRepository = clientRepository;
        this.envoyerRepository=envoyerRepository;
        this.getDevise=getDevise;
    }

    public byte[] genererRelevePDF(String numTel, int month,int year) {
        try {
            Client client = clientRepository.findById(numTel).orElseThrow();
            List<Envoyer> envoyer = envoyerRepository.getTransactionByMonth(numTel,month,year);
            String devise = getDevise.getCurrencyName(client.getPays());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Date en haut à droite
            LocalDate dateActuelle = LocalDate.of(year,month,1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            Paragraph dateParagraph = new Paragraph("Date : " + dateActuelle.format(formatter))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(dateParagraph);

            // Informations personnelles
            document.add(new Paragraph("Contact : " + client.getNumTel()));
            document.add(new Paragraph(client.getNom()));
            document.add(new Paragraph(client.getSexe()));
            document.add(new Paragraph("Solde actuel : " + client.getSolde() +" "+ devise)
                    .setMarginBottom(20));

            // Tableau des transactions
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 3, 2}));
            table.setWidth(UnitValue.createPercentValue(100));

            // En-têtes du tableau
            table.addHeaderCell(new Cell().add(new Paragraph("Date")).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Raison")).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Nom du récepteur")).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Montant")).setTextAlignment(TextAlignment.CENTER));

            // Données du tableau
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Envoyer transaction : envoyer) {
                table.addCell(new Cell().add(new Paragraph(transaction.getDate().format(dateFormatter)))).setTextAlignment(TextAlignment.CENTER);
                table.addCell(new Cell().add(new Paragraph(transaction.getRaison()))).setTextAlignment(TextAlignment.CENTER);
                table.addCell(new Cell().add(new Paragraph(clientRepository.findById(transaction.getNumRecepteur()).get().getNom()))).setTextAlignment(TextAlignment.CENTER);
                table.addCell(new Cell().add(new Paragraph(String.valueOf(transaction.getMontant()))).setTextAlignment(TextAlignment.CENTER));
            }

            document.add(table);

            // Total débit
            BigDecimal totalDebit = getTotalValue(envoyer);
            Paragraph totalParagraph = new Paragraph("Total Débit : " + totalDebit +" "+ devise)
                    .setMarginTop(10)
                    .setBold();
            document.add(totalParagraph);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private BigDecimal getTotalValue(List<Envoyer> envoyers){
        BigDecimal total=new BigDecimal(0);
        for(Envoyer envoyer : envoyers){
            total=total.add(envoyer.getMontant());
        }
        return total;
    }
}
