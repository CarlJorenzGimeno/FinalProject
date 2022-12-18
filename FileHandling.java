package FinalProject;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.ArrayList;

public class FileHandling {

    //Check if save file exists
    public boolean saveExists(){
        return new File("bread.sav").isFile();
    }

    //Encode save
    public void encodeSave(ArrayList<Object> save) throws IOException, InvalidAlgorithmParameterException {
        SecureRandom secureRandom = new SecureRandom();
        File file_path = new File("bread.sav");
        //Creates save if it doesn't exist
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

            //Initialize cipher
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, k,ivspec);

            //Initialize FileOutputStream and write the AES key and IV
            FileOutputStream fos = new FileOutputStream(file_path);
            fos.write(iv);
            fos.write(k.getEncoded());
            fos.flush();
            //Cipher save using AES 128
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
            //Initialize save container and cipher
            save = new ArrayList<Object>();
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            FileInputStream fis = new FileInputStream(new File("bread.sav"));
            //Read key and IV
            byte[] iv = fis.readNBytes(16);
            byte[] k = fis.readNBytes(16);
            Key key = new SecretKeySpec(k, 0, k.length, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            aes.init(Cipher.DECRYPT_MODE, key,ivspec);

            //Decrypt save file
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
        //Obtain save
        ArrayList<Object> save = decodeSave();

        //Container for parsed save and temporary one for conversion
        ArrayList<Object> temp = new ArrayList<Object>();
        ArrayList<Object> temp1 = (ArrayList<Object>) save.get(0);
        ArrayList<Integer> upgrades = new ArrayList<Integer>();
        ArrayList<Boolean> perm = new ArrayList<Boolean>();

        //Convert ArrayList<Object> to its appropriate data type
        for(Object o:temp1){
            upgrades.add((Integer) o);
        }
        temp1 = (ArrayList<Object>) save.get(1);
        for(Object o:temp1){
            perm.add((Boolean) o);
        }

        //Add to temp ArrayList and return
        temp.add(upgrades);
        temp.add(perm);
        return temp;
    }
    public ArrayList<Relic> parseRelics() throws InvalidAlgorithmParameterException {
        ArrayList<Object> save = decodeSave();
        ArrayList<Relic> relics = new ArrayList<Relic>();
        //Add relics to game
        for(int i = 2; i < save.size(); i++){
            relics.add((Relic) save.get(i));
        }
        return relics;
    }
    public void composeSave(ArrayList<Building> buildings, ArrayList<Relic> relics) throws IOException, InvalidAlgorithmParameterException {
        //Compile all necessary data to an ArrayList and encode it.
        Bread bread = new Bread("dummy",0);
        ArrayList<Object> save = new ArrayList<Object>();
        ArrayList<Integer> upgrades = new ArrayList<Integer>();
        ArrayList<Boolean> perm = new ArrayList<Boolean>();
        upgrades.add(bread.getBread());
        //Only these two attributes are required to restore all progress
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
