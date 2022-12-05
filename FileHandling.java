package FinalProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;


public class FileHandling {

    public boolean saveExists(){
        return new File("C:/Bread/test.sav").isFile();
    }
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

    public ArrayList<Object> parseSave() throws IOException {
        String save = decodeSave();
        String[] split_save = save.split(",");
        ArrayList<Object> values = new ArrayList<>();
        for (int i=0; i<split_save.length;i++){
            try{
                values.add(Integer.parseInt(split_save[i]));
            }
            catch (NumberFormatException e){
                values.add(split_save[i]);
            }
        }
        return values;
    }
    public void composeSave(){

    }
}
