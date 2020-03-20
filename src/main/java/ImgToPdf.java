import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImgToPdf {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(ImgToPdf.class);

    public static void main(String[] args) {
        new ImgToPdf(args[0], args[1]);
    }

    public ImgToPdf(final String inputFolder, final String outputFolder) {
        try {
            Files.list(Paths.get(inputFolder)).forEach(file -> {
                if(!file.toFile().isDirectory()) {
                    final String fileName = file.getFileName().toString();
                    final String targetFile = fileName.substring(0, fileName.length() - 3) + "pdf";
                    convert(file.toString(), outputFolder + targetFile);
                }
            });
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private void convert(final String input, final String output) {
        try (final FileOutputStream fos = new FileOutputStream(output)) {
            final BufferedImage bufferedImage = ImageIO.read(new File(input));
            final Document document = new Document(new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight()),
                    0, 0, 0, 0);
            final PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            document.add(Image.getInstance(input));
            document.close();
            writer.close();
        } catch (IOException | DocumentException e) {
            LOG.error(e);
        }
    }
}
