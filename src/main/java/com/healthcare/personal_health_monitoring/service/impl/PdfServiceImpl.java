package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.repository.NoteRepository;
import com.healthcare.personal_health_monitoring.service.PdfService;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final NoteRepository noteRepository;

    @Override
    public byte[] generatePatientHistoryPdf(Long patientId,
                                            LocalDate from,
                                            LocalDate to) {

        List<Note> notes =
                noteRepository.findByPatientIdAndVisitDateBetween(
                        patientId, from, to
                );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Patient Medical History"));
            document.add(new Paragraph("From: " + from + " To: " + to));
            document.add(new Paragraph(" "));

            for (Note note : notes) {
                document.add(new Paragraph("Date: " + note.getVisitDate()));
                document.add(new Paragraph("Doctor: " + note.getDoctor().getFullName()));
                document.add(new Paragraph("Note: " + note.getDescription()));
                document.add(new Paragraph("Prescription: " + note.getFileUrl()));
                document.add(new Paragraph("-----------------------------------"));
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }

        return out.toByteArray();
    }
}

