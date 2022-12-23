package FinalProject;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Bread{
    private final String[] names = {"Pandesal","Monay","Mamon","Ensaymada","Pan de Coco","Hopia"};
    private static int bread;
    private static int breadType;


    public int getBread(){return bread;}
    public String getName(){return names[breadType];}
    public static void setBread(int bread) {
        Bread.bread = bread;
        updateBreadType();
    }
    //Update Breadtype depending on the no of bread
    private static void updateBreadType(){
        double temp = Math.log(bread) / Math.log(100);
        if (Double.isFinite(temp)){
            breadType = Math.min((int) temp, 5);
        }
    }

    public void addBread(int num){bread += num; updateBreadType();}
    public void removeBread(int cost){bread -= cost; updateBreadType();}
    public int getBreadPerClick(){
        updateBreadType();
        Double temp = (breadType+1)*(1+Relic.getTotal_relic_click_value());
        return temp.intValue();
    }

    public JPanel getPanel(BufferedImage icon){
        //Create JPanel and set layout to BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        //Total Bread, Bread per second, and Bread per click
        JLabel current = new JLabel("Bread: " + names[breadType]);
        JLabel total_bread = new JLabel(bread+" Bread");
        JLabel bpc = new JLabel((getBreadPerClick()+" bread per click"));
        JLabel bps = new JLabel(Building.getTotal()+" bread per second");

        //Button for clicking
        JButton button = new JButton(new ImageIcon(icon));
        //Add ActionListener
        button.addActionListener(e -> {
            //Add Bread every click using bread per click
            addBread(getBreadPerClick());
            updateBreadType();

            //Update text values
            total_bread.setText("<html><center>"+bread+" Bread</center></html>");
            bpc.setText("<html><center>"+getBreadPerClick()+" bread per click.</center></html>");
            bps.setText("<html><center>"+Building.getTotal()+" bread per second.</center></html>");

            //Update panel
            panel.revalidate();
            panel.repaint();
        });
        //Add components
        panel.add(current);
        panel.add(total_bread);
        panel.add(bpc);
        panel.add(bps);
        panel.add(button);
        return panel;
    }

    public Bread(int breadType){
        Bread.breadType = breadType;
    }
}
