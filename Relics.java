package FinalProject;

public class Relics {
    private String relics_name;
    private double relics_bps_value;
    private double relics_click_value;
    private static double total_relic_bps_value;
    private static double total_relic_click_value;

    public String getRelics_name() {
        return relics_name;
    }

    public double getRelics_bps_value() {
        return relics_bps_value;
    }

    public double getRelics_click_value() {
        return relics_click_value;
    }

    public static double getTotal_relic_bps_value() {
        return total_relic_bps_value;
    }

    public static double getTotal_relic_click_value() {
        return total_relic_click_value;
    }

    public Relics(String name, double bps_value, double click_value){
        relics_name = name;
        relics_bps_value = bps_value;
        relics_click_value = click_value;
    }
}
