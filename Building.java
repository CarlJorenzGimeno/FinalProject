package FinalProject;


import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Building{
    Bread bread = new Bread("",0);
    private final String name;
    private final int initial_cost;
    private String permUpgrade;
    private Boolean isUpgraded = false;
    private int noOfBuildings;
    private final int output;
    private static int total_output;

    public int getCost() {
        //Formula for cost: P*(1+R)^N*(1+R2)
        return (int) (initial_cost*Math.pow(1.1,noOfBuildings)*(1+Relic.getTotal_relic_bps_value()));
    }

    public int getNoOfBuildings() {
        return noOfBuildings;
    }
    public void setNoOfBuildings(int num){
        noOfBuildings = num;
        total_output += output*num;
    }

    public void setUpgraded(Boolean upgraded) {
        isUpgraded = upgraded;
    }

    public Boolean getUpgraded() {
        return isUpgraded;
    }

    public void addBuilding(int num){
        noOfBuildings += num;
        total_output += output*num;
    }

    public static int getTotal(){return total_output;}

    public int getOutput() {return output*noOfBuildings;}
    public String getName(){return name;}
    public String getPermUpgrade(){return permUpgrade;}
    public JButton getUpgrade(){return new JButton("<html><center>Upgrade<br>"+getCost()+"$</center></html>");}
    public boolean buyUpgrade(){
        if(bread.getBread()>=getCost()){
            bread.removeBread(getCost());
            addBuilding(1);
            return true;
        }
        else{return false;}
    }
    public boolean buyPermUpgrade(){
        if(bread.getBread() >= initial_cost*120){
            bread.removeBread(initial_cost*120);
            isUpgraded = true;
        }
        return isUpgraded;
    }
    public JPanel upgradePanel(){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("<html><center>"+getName()+"</center></html>");
        JButton upgrade = getUpgrade();
        upgrade.addActionListener (e -> {
            Boolean isBuySuccess = buyUpgrade();
            if (!isBuySuccess){JOptionPane.showMessageDialog(null, "Insufficient Bread.");}
            upgrade.setText("<html><center>Upgrade<br>"+getCost()+"$</center></html>");
            panel.revalidate();
            panel.repaint();

        });
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(upgrade);
        return panel;
    }

    public JPanel permUpgradePanel(){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("<html><center>"+permUpgrade+"</center></html>");
        JButton upgrade = new JButton();
        if (!isUpgraded){
            upgrade.setText("<html><center>Upgrade<br>"+initial_cost*123+"$</center></html>");
        }
        else{
            upgrade.setText("<html><center>Bought<br>0$</center></html>");
            upgrade.setEnabled(false);
        }
        upgrade.addActionListener (e -> {
            isUpgraded = buyPermUpgrade();
            if (!isUpgraded) JOptionPane.showMessageDialog(null, "Insufficient Bread.");
            panel.revalidate();
            panel.repaint();

        });
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(upgrade);
        return panel;
    }

    public Building(String name, int initial_cost, int output, int noOfBuildings, String permUpgrade){
        this.name = name;
        this.initial_cost = initial_cost;
        this.output = output;
        this.noOfBuildings = noOfBuildings;
        this.permUpgrade = permUpgrade;
        total_output = getOutput();
    }
}
