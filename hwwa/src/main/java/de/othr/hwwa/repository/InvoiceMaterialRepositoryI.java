package de.othr.hwwa.repository;

import de.othr.hwwa.model.Invoice;
import de.othr.hwwa.model.InvoiceMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceMaterialRepositoryI extends JpaRepository<InvoiceMaterial,Long> {

    public List<InvoiceMaterial> findInvoiceMaterialByInvoice(Invoice invoice);
    public void deleteInvoiceMaterialByInvoice(Invoice invoice);

}
