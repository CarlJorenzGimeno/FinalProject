package FinalProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


public class GUI {
    private static int menu = 0;

    FileHandling fh = new FileHandling();


    private void restoreProgress(){
    }

    private int calculateBPS(){
        return 0;
    }

    public static void main(String args[]) throws IOException {

        //Creating the Frame
        JFrame frame = new JFrame("PanDeSal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Options");
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
        upgrades.addActionListener(e -> menu = 1);
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
        if (menu == 0){
            BufferedImage Pan = ImageIO.read(GUI.class.getResource("pan.png"));;
            JLabel picture = new JLabel(new ImageIcon(Pan));
            scroll.add(picture);
        }
        else if(menu == 1){

        }

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, scroll);
        frame.setVisible(true);
    }


}
