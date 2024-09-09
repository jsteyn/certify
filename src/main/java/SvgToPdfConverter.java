import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SvgToPdfConverter {
    public static void main(String[] args) {
        try {
            // Step 1: Load and parse the SVG file to get dimensions
            String svgFilePath = "/Users/janne/.certify/1.svg"; // Change to your SVG file path
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            InputStream svgInputStream = new FileInputStream(new File(svgFilePath));
            SVGDocument svgDocument = factory.createSVGDocument(svgFilePath, svgInputStream);
            SVGSVGElement rootElement = svgDocument.getRootElement();
            float svgWidth = Float.parseFloat(rootElement.getAttribute("width").replaceAll("px", ""));
            float svgHeight = Float.parseFloat(rootElement.getAttribute("height").replaceAll("px", ""));

            // Step 1: Convert SVG to PNG
            String svgUriInput = "file:/Users/janne/.certify/1.svg"; // Change to your SVG file path
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            TranscoderInput inputSvgImage = new TranscoderInput(svgUriInput);
            TranscoderOutput outputPngImage = new TranscoderOutput(pngOutputStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.transcode(inputSvgImage, outputPngImage);

            byte[] pngData = pngOutputStream.toByteArray();

            // Step 2: Write PNG to PDF
            String pdfOutputPath = "1.pdf"; // Change to your desired PDF output path
            PdfWriter writer = new PdfWriter(pdfOutputPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            PageSize pageSize = new PageSize(svgWidth, svgHeight);
            pdfDoc.setDefaultPageSize(pageSize);
            Document document = new Document(pdfDoc);

            // Convert PNG byte array to Image and add to PDF
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(
                    com.itextpdf.io.image.ImageDataFactory.create(pngData)
            );

            // Add image to PDF document
            document.add(image);
            document.close();

            System.out.println("SVG converted to PDF successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
