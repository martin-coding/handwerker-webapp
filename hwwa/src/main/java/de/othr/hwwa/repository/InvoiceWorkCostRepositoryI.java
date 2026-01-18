package de.othr.hwwa.repository;

import de.othr.hwwa.model.Invoice;
import de.othr.hwwa.model.InvoiceWorkCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceWorkCostRepositoryI extends JpaRepository<InvoiceWorkCost,  Long> {
    public List<InvoiceWorkCost> findInvoiceWorkCostByInvoice(Invoice invoice);
    public List<InvoiceWorkCost> findInvoiceWorkCostByInvoice_Id(long invoiceId);
    public void deleteInvoiceWorkCostByInvoice(Invoice invoice);
}
