package org.stockmaster3000.stockmaster3000.components;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PDFGenerator {

    public static void saveTextToPDF(String text, String fileName) {
        String homeDirectory = System.getProperty("user.home");  // Get user home directory
        String downloadsDirectory;

        // Check if on Mac or Windows
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            downloadsDirectory = homeDirectory + "\\Downloads\\"; // Windows Downloads
        } else {
            downloadsDirectory = homeDirectory + "/Downloads/"; // Mac/Linux Downloads
        }

        File file = new File(downloadsDirectory + fileName); // Full file path

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Ensure the font path is correct or use the default font if not found
            File fontFile = new File("src/main/resources/LiberationSans-Regular.ttf");
            PDType0Font font;
            if (fontFile.exists()) {
                font = PDType0Font.load(document, fontFile);
            } else {
                font = PDType0Font.load(document, new File("src/main/resources/Arial.ttf"));
            }

            contentStream.setFont(font, 12);
            contentStream.beginText();

            // Set starting position for the text (100px from left, 700px from top)
            contentStream.newLineAtOffset(30, 700); 

            // Split the text by newlines and print each line
            String[] lines = text.split("\n");
            for (String line : lines) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);  
            }

            contentStream.endText();
            contentStream.close();

            // Save PDF to the determined directory
            document.save(file);
            document.close();

            System.out.println("PDF saved to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
