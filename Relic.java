package FinalProject;

import java.io.Serial;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Relic implements Serializable {
    @Serial
    private static final long serialVersionUID =1L;
    private final String relic_name;
    private final double relic_bps_value;
    private final double relic_click_value;
    private static double total_relic_bps_value;
    private static double total_relic_click_value;
    private static int noOfRelics;

    public String getRelic_name() {
        return relic_name;
    }

    public double getRelic_bps_value() {
        return relic_bps_value;
    }

    public double getRelic_click_value() {
        return relic_click_value;
    }

    public static void setTotal_relic_bps_value(double bps_value){total_relic_bps_value = bps_value;}
    public static double getTotal_relic_bps_value() {
        return total_relic_bps_value;
    }
    public static void setTotal_relic_click_value(double click_value){total_relic_click_value = click_value;}
    public static double getTotal_relic_click_value() {
        return total_relic_click_value;
    }
    public static void setNoOfRelics(int num){noOfRelics = num;}

    public static int getNoOfRelics() {return noOfRelics;}

    public static Relic createRelic(ArrayList<Building> buildings){
        Random rand = new Random();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        Relic relic = null;
        int score = 0;
        //Calculate score
        for(Building building:buildings){
            score += building.getNoOfBuildings()*(buildings.indexOf(building)+1);
        }
        //Convert score to corresponding weights for rng.
        double weightedScore = Double.parseDouble(df.format((double) score/10000));
        //Try to create a relic.
        //If score is low enough that weightedScore is equal to zero,
        //do nothing.
        try {
            relic = new Relic("Relic #" + noOfRelics + 1, rand.nextDouble(weightedScore), rand.nextDouble(weightedScore));
        }
        //Added so throwing IllegalArgumentException isnt necessary on the GUI class.
        catch(IllegalArgumentException ignored){}
        return relic;
    }
    public static void flushRelic(){
        noOfRelics = 0;
        total_relic_click_value = 0;
        total_relic_bps_value = 0;
    }

    public Relic(String name, double bps_value, double click_value){
        relic_name = name;
        relic_bps_value = bps_value;
        relic_click_value = click_value;
        total_relic_bps_value += bps_value;
        total_relic_click_value += click_value;
        noOfRelics++;
    }
}
