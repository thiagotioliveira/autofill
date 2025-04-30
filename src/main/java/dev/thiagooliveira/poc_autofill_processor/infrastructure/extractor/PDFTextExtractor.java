package dev.thiagooliveira.poc_autofill_processor.infrastructure.extractor;

import dev.thiagooliveira.poc_autofill_processor.domain.exception.GenericErrorException;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.TextExtractor;
import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;
import dev.thiagooliveira.poc_autofill_processor.domain.extractor.model.DocumentForExtraction;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class PDFTextExtractor implements TextExtractor {

    private static Logger logger = LoggerFactory.getLogger(PDFTextExtractor.class);

    private final ITesseract tesseract;

    public PDFTextExtractor(ITesseract tesseract) {
        this.tesseract = tesseract;
    }

    @Override
    public RawDocument extract(DocumentForExtraction document) {
        logger.debug("extracting text from pdf");
        try (PDDocument doc = Loader.loadPDF(IOUtils.toByteArray(document.value()))){
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc).trim();
            if(!text.isEmpty()) {
                logger.debug("raw pdf content:\n{}", text);
                return new RawDocument(text);
            }
            StringBuilder sb = new StringBuilder();
            PDFRenderer renderer = new PDFRenderer(doc);
            for (int page = 0; page < doc.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 200);
                sb.append(this.tesseract.doOCR(image));
            }
            String imageText = sb.toString();
            logger.debug("raw pdf content:\n{}", imageText);
            return new RawDocument(imageText);
        } catch (IOException | TesseractException e) {
            throw new GenericErrorException(e);
        }
    }
}
