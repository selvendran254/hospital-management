package com.hospital.management.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class SimplePdfBuilder {
    private SimplePdfBuilder() {
    }

    public static byte[] fromLines(List<String> lines) {
        List<String> safeLines = lines == null ? List.of() : lines;
        StringBuilder contentStream = new StringBuilder("BT /F1 11 Tf 50 780 Td 14 TL ");
        for (String line : safeLines) {
            String cleaned = (line == null ? "" : line).replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
            contentStream.append("(").append(cleaned).append(") Tj T* ");
        }
        contentStream.append("ET");
        byte[] streamBytes = contentStream.toString().getBytes(StandardCharsets.US_ASCII);

        List<String> objects = new ArrayList<>();
        objects.add("1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj");
        objects.add("2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj");
        objects.add("3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >> endobj");
        objects.add("4 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> endobj");
        objects.add("5 0 obj << /Length " + streamBytes.length + " >> stream\n" +
                new String(streamBytes, StandardCharsets.US_ASCII) + "\nendstream endobj");

        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> offsets = new ArrayList<>();
        offsets.add(0);
        for (String object : objects) {
            offsets.add(pdf.length());
            pdf.append(object).append("\n");
        }
        int xrefPos = pdf.length();
        pdf.append("xref\n0 ").append(objects.size() + 1).append("\n");
        pdf.append("0000000000 65535 f \n");
        for (int i = 1; i < offsets.size(); i++) {
            pdf.append(String.format("%010d 00000 n \n", offsets.get(i)));
        }
        pdf.append("trailer << /Size ").append(objects.size() + 1).append(" /Root 1 0 R >>\n");
        pdf.append("startxref\n").append(xrefPos).append("\n%%EOF");
        return pdf.toString().getBytes(StandardCharsets.US_ASCII);
    }
}
