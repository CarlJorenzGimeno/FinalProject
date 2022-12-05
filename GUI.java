package FinalProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GUI extends JFrame implements ActionListener {
    static int menu=0;
    FileHandling fh = new FileHandling();
    Bread pandesal = new Bread("Pandesal",1);
    Bread monay = new Bread("Monay", 1);
    Bread mamon = new Bread ("Mamon", 2);
    Bread ensaymada = new Bread("Ensaymada", 3);
    Bread pandecoco = new Bread("Pan de Coco", 4);
    Bread hopia = new Bread ("Hopia", 5);
    Building clicker = new Building("Clicker",10,1);
    Building baker = new Building("Baker",100,1);
    Building kitchen = new Building("Kitchen",1000,5);
    Building bakery = new Building("Bakery",10000,25);
    Building street = new Building("Street",100000,125);
    Building town = new Building("Town",1000000,625);
    Building city = new Building("City",10000000,3125);

    static ArrayList<Building> buildings = new ArrayList<Building>();
    private void initializeUpgrades() {
        buildings.add(clicker);
        buildings.add(baker);
        buildings.add(kitchen);
        buildings.add(bakery);
        buildings.add(street);
        buildings.add(town);
        buildings.add(city);
    }

    private void restoreProgress() throws IOException {
        if (fh.saveExists()){

        }
    }

    private int calculateBPS(){
        return 0;
    }

    public void updatePanel(JPanel panel){
        panel.revalidate();
        panel.repaint();
    }
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        revalidate();
        repaint();
    }
    public void changeMenu(JPanel panel, Bread bread) throws IOException {
        //Buffer image
        BufferedImage Pan = ImageIO.read(GUI.class.getResource("pan.png"));;
        JPanel picture = bread.getPanel(Pan);

        //Group for upgrades
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group,BoxLayout.Y_AXIS));
        group.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        if (menu == 0) {
            panel.removeAll();
            panel.add(picture);
            updatePanel(panel);
        }
        else if (menu==1){
            panel.removeAll();
            group.add(bread.getBreadLabel());
            for(int i =0; i < buildings.size();i++){group.add(buildings.get(i).getPanel());}
            panel.add(group);
            updatePanel(panel);
        }
    }
    public static void main(String args[]) throws IOException {
        GUI gui = new GUI();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Bread current_bread = gui.pandesal;
        gui.initializeUpgrades();
        gui.setTitle("PanDeSal");
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(400, 400);

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

        // Text Area at the Center
        //Create panel
        JPanel panell = new JPanel();;
        JScrollPane scroll = new JScrollPane(panell);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        Runnable menu = new Runnable() {
            @Override
            public void run() {
                try {
                    gui.changeMenu(panell,current_bread);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gui.updatePanel(panell);
            }
        };
        Runnable bps = new Runnable() {
            @Override
            public void run() {
                current_bread.addBread(Building.getTotal());
            }
        };
        executor.scheduleWithFixedDelay(menu,0,100,TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(bps,0,1,TimeUnit.SECONDS);
        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton home = new JButton("Home");
        home.addActionListener(e -> {gui.menu = 0;});
        JButton upgrades = new JButton("Upgrades");
        upgrades.addActionListener(e -> {gui.menu = 1;});
        JButton perm = new JButton("Perm. Upgrades");
//        perm.addActionListener(e -> {});
        JButton relics = new JButton("Relics");// Components Added using Flow Layout
        panel.add(home);
        panel.add(upgrades);
        panel.add(perm);
        panel.add(relics);
        panel.revalidate();
        panel.repaint();


        //Adding Components to the frame.
        gui.getContentPane().add(BorderLayout.SOUTH, panel);
        gui.getContentPane().add(BorderLayout.NORTH, mb);
        gui.getContentPane().add(BorderLayout.CENTER, scroll);
        gui.setVisible(true);
    }


}
