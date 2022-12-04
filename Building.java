package FinalProject;



public class Building {
    private String name;
    private int initial_cost;
    private double cost_multiplier;
    private int noOfBuildings;
    private double relicBuffs = Relics.getTotal_relic_bps_value();

    public String getName() {
        return name;
    }

    public int getCost() {
        return (int) (initial_cost*Math.pow(1+cost_multiplier,noOfBuildings)*(1+relicBuffs));
    }

    public int getNoOfBuildings() {
        return noOfBuildings;
    }

    public void addBuilding(int num){
        noOfBuildings += num;
    }

    public Building(String name, int initial_cost, double cost_multiplier){
        this.name = name;
        this.initial_cost = initial_cost;
        this.cost_multiplier = cost_multiplier;
    }
}
