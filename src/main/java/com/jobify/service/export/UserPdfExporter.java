package com.jobify.service.export;

import com.jobify.payload.request.UserDTO;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter
{
    private static final Logger LOGGER = LogManager.getLogger(UserPdfExporter.class.getName());

    public void export(List<UserDTO> listUsers, HttpServletResponse response) throws IOException
    {
        super.setResponseHeader(response, "application/pdf", ".pdf", "users_");

        Document document = new Document(PageSize.A3);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of Users", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(20);
        table.setWidths(new float[]{0.5f, 2.0f, 1.5f, 1.5f, 0.5f, 1.5f});

        writeTableHeader(table);
        writeTableData(table, listUsers);

        document.add(table);

        LOGGER.info("{}", "Pdf file downloaded successfully!");

        document.close();

    }

    private void writeTableData(PdfPTable table, List<UserDTO> listUsers)
    {
        for (UserDTO user : listUsers)
        {
            table.getDefaultCell()
                 .setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell()
                 .setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell()
                 .setPadding(5);
            table.addCell(String.valueOf(user.getUserID()));
            table.addCell(user.getEmailID());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getAge()
                              .toString());
            table.addCell(user.getLocation());
        }
    }

    private void writeTableHeader(PdfPTable table)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Age", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Location", font));
        table.addCell(cell);
    }

}
