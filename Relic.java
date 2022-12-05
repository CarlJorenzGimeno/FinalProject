package FinalProject;

public class Relic {
    private String relic_name;
    private double relic_bps_value;
    private double relic_click_value;
    private static double total_relic_bps_value;
    private static double total_relic_click_value;

    public String getRelic_name() {
        return relic_name;
    }

    public double getRelic_bps_value() {
        return relic_bps_value;
    }

    public double getRelic_click_value() {
        return relic_click_value;
    }

    public static double getTotal_relic_bps_value() {
        return total_relic_bps_value;
    }

    public static double getTotal_relic_click_value() {
        return total_relic_click_value;
    }

    public Relic(String name, double bps_value, double click_value){
        relic_name = name;
        relic_bps_value = bps_value;
        relic_click_value = click_value;
        total_relic_bps_value += bps_value;
        total_relic_click_value += click_value;
    }
}
