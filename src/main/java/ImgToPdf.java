import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class ImgToPdf {

    public static void main(String[] args) {
        new ImgToPdf(Paths.get(args[0]), Paths.get(args[1]));
    }

    public ImgToPdf(final Path inputFolder, final Path outputFolder) {
        try (final Stream<Path> files = Files.list(inputFolder)) {
            files.filter(Files::isRegularFile)
                    .forEach(file -> {
                        final String output = Paths.get(outputFolder.toString(), FilenameUtils.getBaseName(file.getFileName().toString()) + ".pdf").toString();
                        try (final FileOutputStream fos = new FileOutputStream(output)) {
                            final BufferedImage bufferedImage = ImageIO.read(file.toFile());
                            final Rectangle rectangle = new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight());
                            final Document document = new Document(rectangle, 0, 0, 0, 0);
                            final PdfWriter writer = PdfWriter.getInstance(document, fos);
                            writer.open();
                            document.open();
                            document.add(Image.getInstance(file.toString()));
                            document.close();
                            writer.close();
                        } catch (IOException | DocumentException e) {
                            log.error("Error", e);
                        }
                    });
        } catch (IOException e) {
            log.error("Error", e);
        }
    }
}
