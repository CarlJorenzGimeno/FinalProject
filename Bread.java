package FinalProject;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Bread{
    private final String name;
    private static int bread;
    private final int breadPerClick;


    public int getBread(){return bread;}
    public String getName(){return name;}

    public static void setBread(int bread) {Bread.bread = bread;}

    public void addBread(int num){bread = bread+num;}
    public void removeBread(int cost){bread -= cost;}
    public int getBreadPerClick(){return (int) (breadPerClick*(1+Relic.getTotal_relic_click_value()));}

    public JPanel getPanel(BufferedImage icon){
        //Create JPanel and set layout to BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        //Total Bread, Bread per second, and Bread per click
        JLabel total_bread = new JLabel(bread+" Bread");
        JLabel bpc = new JLabel(breadPerClick+" bread per click");
        JLabel bps = new JLabel(Building.getTotal()+" bread per second");

        //Button for clicking
        JButton button = new JButton(new ImageIcon(icon));
        //Add ActionListener
        button.addActionListener(e -> {
            //Add Bread every click using bread per click
            addBread(getBreadPerClick());

            //Update text values
            total_bread.setText("<html><center>"+bread+" Bread</center></html>");
            bpc.setText("<html><center>"+breadPerClick+" bread per click.</center></html>");
            bps.setText("<html><center>"+Building.getTotal()+" bread per second.</center></html>");

            //Update panel
            panel.revalidate();
            panel.repaint();
        });
        //Add components
        panel.add(total_bread);
        panel.add(bpc);
        panel.add(bps);
        panel.add(button);
        return panel;
    }

    public Bread(String name, int breadPerClick){
        this.name = name;
        this.breadPerClick = breadPerClick;
    }
}
