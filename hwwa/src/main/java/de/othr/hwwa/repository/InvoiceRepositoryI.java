package de.othr.hwwa.repository;

import de.othr.hwwa.model.Company;
import de.othr.hwwa.model.Invoice;
import de.othr.hwwa.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepositoryI extends JpaRepository<Invoice,  Long> {
    public Optional<Invoice> findInvoiceByTask(Task task);
    public List<Invoice> findByCompanyId(long companyId);
}
