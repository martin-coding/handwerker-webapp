package de.othr.hwwa.service;

public interface PdfServiceI {
    byte[] buildTaskPdf(long taskId);
}