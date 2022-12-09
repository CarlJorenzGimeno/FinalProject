package FinalProject;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class FileHandling {

    public boolean saveExists(){
        return new File("C:/Bread/bread.sav").isFile();
    }
    private void encodeSave(ArrayList<Object> save) {
        try {
            Key k = KeyGenerator.getInstance("AES").generateKey();
            String encodedkey = Base64.getEncoder().encodeToString(k.getEncoded());
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, k);
            FileOutputStream fos = new FileOutputStream(new File("C:/Bread/bread.sav"));
            fos.write(encodedkey.getBytes());
            fos.flush();
            CipherOutputStream cos = new CipherOutputStream(fos, aes);
            ObjectOutputStream oos = new ObjectOutputStream(cos);
            oos.writeObject(save.size());
            for (Object o : save) {oos.writeObject(o);}
            oos.flush();
            oos.close();
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex){
            ex.printStackTrace();
        }
    }

    private ArrayList<Object> decodeSave() {
        ArrayList<Object> save = null;
        try {
            save = new ArrayList<Object>();
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            FileInputStream fis = new FileInputStream(new File("C:/Bread/bread.sav"));
            byte[] decoded = Base64.getDecoder().decode(fis.readNBytes(24));
            Key kk = new SecretKeySpec(decoded, 0, decoded.length, "AES");
            aes.init(Cipher.DECRYPT_MODE, kk);
            CipherInputStream cis = new CipherInputStream(fis, aes);
            ObjectInputStream ois = new ObjectInputStream(cis);
            int size = (int) ois.readObject();
            for (int i = 0; i < size; i++) save.add(ois.readObject());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException |
                 ClassNotFoundException e) {
            e.printStackTrace();
        }
        return save;
    }

    public ArrayList<Object> parseSave(ArrayList<Object> save) throws IOException{
        String sav = (String) save.get(0);
        String[] split_save = sav.split("\\|");
        ArrayList<Object> values = new ArrayList<>();
        for (String s : split_save)
            try {values.add(Integer.parseInt(s));}
            catch (NumberFormatException e) {values.add(s);}
        return values;
    }
    public ArrayList<Object> parseRelics(ArrayList<Object> save) {
        ArrayList<Object> sav = new ArrayList<Object>();
        for(int i = 1; i < save.size(); i++){
            sav.add(save.get(i));
        }
        return sav;
    }
    public ArrayList<Object> composeSave(ArrayList<Building> buildings, ArrayList<Relic> relics){
        Bread bread = new Bread("dummy",0);
        ArrayList<Object> save = new ArrayList<Object>();
        String upgrades = "";
        upgrades = upgrades.concat(bread.getBread()+"|");
        for (Building building : buildings) upgrades = upgrades.concat(building.getNoOfBuildings() + "\\|");
        save.add(upgrades);
        save.addAll(relics);
        return save;
    }
}
