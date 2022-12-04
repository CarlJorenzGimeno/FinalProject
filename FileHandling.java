package FinalProject;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;


public class FileHandling {
    private void encodeSave(String save) throws IOException {
        Base64.Encoder enc = Base64.getEncoder();
        byte[] encoded = enc.encode(save.getBytes("UTF-8"));
        Files.write(Path.of("C:/Bread/test.sav"), encoded);
    }

    private String decodeSave() throws IOException {
        Base64.Decoder dec = Base64.getDecoder();
        Path path = Paths.get("C:/Bread/test.sav");
        byte[] data = Files.readAllBytes(path);
        String decoded = String.valueOf(dec.decode(data));
        return decoded;
    }

    private void parseSave() throws IOException {
        String save = decodeSave();

    }
}
