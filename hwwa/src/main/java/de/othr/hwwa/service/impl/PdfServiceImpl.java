package de.othr.hwwa.service.impl;

import de.othr.hwwa.model.dto.InvoiceDto;
import de.othr.hwwa.service.PdfServiceI;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Service
public class PdfServiceImpl implements PdfServiceI {

    private final SpringTemplateEngine templateEngine;

    public PdfServiceImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateInvoicePdf(InvoiceDto invoice) throws Exception {

        Context context = new Context();
        context.setVariable("invoice", invoice);

        String html = templateEngine.process("invoices/invoice_pdf", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
