package FinalProject;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Building{
    Bread bread = new Bread("Dummy",0);
    private String name;
    private int initial_cost;

    private int noOfBuildings;
    private int output;
    private static int total_output;


    public String getName() {
        return name;
    }

    public int getCost() {
        //Formula for cost: P*(1+R)^N*(1+R2)
        return (int) (initial_cost*Math.pow(1.1,noOfBuildings)*(1+Relic.getTotal_relic_bps_value()));
    }

    public int getNoOfBuildings() {
        return noOfBuildings;
    }

    public void addBuilding(int num){
        noOfBuildings += num;
        total_output += output;
    }

    public static int getTotal(){return total_output;}

    public int getOutput() {return output*noOfBuildings;}
    public JLabel getLabel(){return new JLabel(name);}
    public JButton getUpgrade(){return new JButton("Upgrade\n"+getCost()+"$");}
    public boolean buyUpgrade(){
        if(bread.getBread()>=getCost()){
            bread.removeBread(getCost());
            addBuilding(1);
            return true;
        }
        else{return false;}
    }
    public JPanel getPanel(){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel(name);
        JButton upgrade = new JButton("<html><center>Upgrade<br>"+getCost()+"$</center></html>");
        upgrade.addActionListener (e -> {
            Boolean isBuySuccess = buyUpgrade();
            if (!isBuySuccess){
                JOptionPane.showMessageDialog(null, "Insufficient Bread.");
            }
            upgrade.setText("<html><center>Upgrade<br>"+getCost()+"$</center></html>");
            panel.revalidate();
            panel.repaint();

        });
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(upgrade);
        return panel;
    }

//    @Override
//    public void actionPerformed(ActionEvent e){
//        Object obj = e.getSource();
//        addBuilding(1);
//    }

    public Building(String name, int initial_cost, int output){
        this.name = name;
        this.initial_cost = initial_cost;
        this.output = output;
    }
}
