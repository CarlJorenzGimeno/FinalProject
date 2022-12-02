package FinalProject;

import javax.swing.*;
import java.awt.*;

class Pandesal extends Bread{

    public double calculateBPS(){
        return Pandesal.this.getBps();
    }
}

public class GUI {
    FileHandling fh = new FileHandling();


    private void restoreProgress(){
    }

    private int calculateBPS(){

    }

    public static void main(String args[]){

        //Creating the Frame
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Save");
        JMenuItem m22 = new JMenuItem("Respec");
        m1.add(m11);
        m1.add(m22);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton upgrades = new JButton("Upgrades");
        JButton perm = new JButton("Perm. Upgrades");
        JButton relics = new JButton("Relics");// Components Added using Flow Layout
        panel.add(upgrades);
        panel.add(perm);
        panel.add(relics);

        // Text Area at the Center
        JTextArea ta = new JTextArea();
        JScrollPane scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, scroll);
        frame.setVisible(true);
    }


}
