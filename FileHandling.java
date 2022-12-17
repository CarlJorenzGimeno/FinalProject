package FinalProject;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.ArrayList;

public class FileHandling {

    public boolean saveExists(){
        return new File("bread.sav").isFile();
    }
    public void encodeSave(ArrayList<Object> save) throws IOException, InvalidAlgorithmParameterException {
        SecureRandom secureRandom = new SecureRandom();
        File file_path = new File("bread.sav");
        file_path.createNewFile();
        try {
            //Generate key
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            SecretKey k = keygen.generateKey();
            //Generate IV
            byte[] iv = new byte[128/8];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

//            String encodedkey = Base64.getEncoder().encodeToString(k.getEncoded());
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, k,ivspec);
            FileOutputStream fos = new FileOutputStream(file_path);
            fos.write(iv);
            fos.write(k.getEncoded());
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

    public ArrayList<Object> decodeSave() throws InvalidAlgorithmParameterException {
        ArrayList<Object> save = null;
        try {
            save = new ArrayList<Object>();
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            FileInputStream fis = new FileInputStream(new File("bread.sav"));
//            byte[] decoded = Base64.getDecoder().decode(fis.readNBytes(24));
            byte[] iv = fis.readNBytes(16);
            byte[] k = fis.readNBytes(16);
            Key key = new SecretKeySpec(k, 0, k.length, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            aes.init(Cipher.DECRYPT_MODE, key,ivspec);
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

    public ArrayList<Object> parseSave() throws IOException, InvalidAlgorithmParameterException {
        ArrayList<Object> save = decodeSave();
        ArrayList<Object> temp = new ArrayList<Object>();
        temp.add(save.get(0));
        temp.add(save.get(1));
        return temp;
    }
    public ArrayList<Relic> parseRelics() throws InvalidAlgorithmParameterException {
        ArrayList<Object> save = decodeSave();
        ArrayList<Relic> relics = new ArrayList<Relic>();
        for(int i = 2; i < save.size(); i++){
            relics.add((Relic) save.get(i));
        }
        return relics;
    }
    public void composeSave(ArrayList<Building> buildings, ArrayList<Relic> relics) throws IOException, InvalidAlgorithmParameterException {
        Bread bread = new Bread("dummy",0);
        ArrayList<Object> save = new ArrayList<Object>();
        ArrayList<Integer> upgrades = new ArrayList<Integer>();
        ArrayList<Boolean> perm = new ArrayList<Boolean>();
        upgrades.add(bread.getBread());
        for (Building building:buildings){
            upgrades.add(building.getNoOfBuildings());
            perm.add(building.getUpgraded());
        }

        save.add(upgrades);
        save.add(perm);
        save.addAll(relics);
        encodeSave(save);
    }
}
