package FinalProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GUI extends JFrame {
    public static int menu=0;
    FileHandling fh = new FileHandling();
    private final Bread pandesal = new Bread("Pandesal",1);
    private final Bread monay = new Bread("Monay", 1);
    private Bread mamon = new Bread ("Mamon", 2);
    private Bread ensaymada = new Bread("Ensaymada", 3);
    private Bread pandecoco = new Bread("Pan de Coco", 4);
    private Bread hopia = new Bread ("Hopia", 5);
    Building clicker = new Building("Clicker",10,1,0,"Rapid Tap");
    Building baker = new Building("Baker",100,5,0,"Metal Rolling Pin");
    Building kitchen = new Building("Kitchen",1000,25,0,"Convection Oven");
    Building bakery = new Building("Bakery",10000,125,0,"More Staff");
    Building street = new Building("Store",100000,625,0,"Add Merchandise");
    Building town = new Building("Town",1000000,3125,0,"Community Service");
    Building city = new Building("City",10000000,15225,0,"City Act");

    static ArrayList<Building> buildings = new ArrayList<Building>();
    static ArrayList<Relic> relics = new ArrayList<Relic>();


    private void restoreProgress() throws IOException, InvalidAlgorithmParameterException {
        //Initialize buildings
        buildings.add(clicker);
        buildings.add(baker);
        buildings.add(kitchen);
        buildings.add(bakery);
        buildings.add(street);
        buildings.add(town);
        buildings.add(city);
        if (fh.saveExists()){
            ArrayList<Object> save = fh.parseSave();
            ArrayList<Integer> upgrades = (ArrayList<Integer>) save.get(0);
            ArrayList<Boolean> perm = (ArrayList<Boolean>) save.get(1);
            Bread.setBread(upgrades.get(0));
            for (int i = 0; i < 7;i++){
                buildings.get(i).setNoOfBuildings(upgrades.get(i+1));
                buildings.get(i).setUpgraded(perm.get(i));
            }
            relics = fh.parseRelics();
        }
    }

    public void updatePanel(JPanel panel){
        panel.revalidate();
        panel.repaint();
    }

    public void changeMenu(JPanel panel, Bread bread) throws IOException {
        //Buffer image
        BufferedImage breadImage = ImageIO.read(Objects.requireNonNull(GUI.class.getResource("pan.png")));;
        JPanel picture = bread.getPanel(breadImage);

        //Bread clicking menu
        if (menu == 0) {
            panel.removeAll();
            panel.add(picture);
            panel.add(new JLabel(bread.getName()));
        }
        //Upgrades menu
        else if(menu == 1) {
            panel.removeAll();
            panel.add(new JLabel(bread.getBread()+" Bread"));
            for (Building building : buildings) {
                panel.add(building.upgradePanel());
            }
        }
        else if (menu == 2) {
            panel.removeAll();
            panel.add(new JLabel(bread.getBread()+" Bread"));
            for (Building building : buildings) {
                panel.add(building.permUpgradePanel());
            }
        }
        else if (menu == 3) {
            panel.removeAll();
            if (relics.size() == 0){
                panel.add(new JLabel("<html><center>No Relics Yet.</center></html>"));
            }
            else{
                JPanel relicSlot = new JPanel(new FlowLayout());
                for(Relic relic: relics){
                    relicSlot.add(new JLabel(relic.getRelic_name()));
                    relicSlot.add(new JLabel("+"+relic.getRelic_bps_value()+"% Bread Per Second"));
                    relicSlot.add(new JLabel("+"+relic.getRelic_click_value()+"% Bread Per Click"));
                }
                panel.add(relicSlot);
            }
        }
//        updatePanel(panel);
    }


    public void createWindow(Bread bread) throws IOException {
        //GUI window Properties
        setTitle("PanDeSal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);

        //Create Menubar
        JMenuBar mb = new JMenuBar();
        JMenu option = new JMenu("Options");
        option.setMnemonic('O');
        JMenuItem save = new JMenuItem("Save");
        JMenuItem respec = new JMenuItem("Respec");
        JMenuItem reset = new JMenuItem("Reset");
        option.add(save);
        option.add(respec);
        option.add(reset);

        JMenu m2 = new JMenu("Help");
        mb.add(option);
        mb.add(m2);

        //Create main panel to house the changing menu elements
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        //Creating the panel at bottom and adding components
        JPanel bottomPanel = new JPanel(); // the panel is not visible in output
        JButton bmenu_home = new JButton("Home");
        JButton bmenu_upgrades = new JButton("Upgrades");
        JButton bmenu_perm = new JButton("Perm. Upgrades");
        JButton bmenu_relic = new JButton("Relics");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();

                //Menubar actions
                //Save
                if(source == save){
                    try {fh.composeSave(buildings,relics);}
                    catch (IOException | InvalidAlgorithmParameterException ex) {throw new RuntimeException(ex);}
                }
                //Respec
                //Restart with a randomly generated relic.
                else if(source == respec){
                    Relic new_relic = Relic.createRelic(buildings);
                    if (new_relic != null){
                        relics.add(new_relic);
                        Bread.setBread(0);
                        for(Building building:buildings){building.setNoOfBuildings(0);}
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Insufficient progress.");
                    }
                }
                //Reset progress
                else if(source == reset){
                    int choice = JOptionPane.showConfirmDialog(null,"This will reset all your progress until now.\nAre you sure you want to reset your progress.\nTHIS PROCESS CANNOT BE UNDONE.","Confirm Reset",JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION){
                        Bread.setBread(0);
                        for(Building building:buildings){building.setNoOfBuildings(0);}
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Cancelled");
                    }
                }

                //Change main panel based on chosen option
                if (source == bmenu_home){menu = 0;}
                else if (source == bmenu_upgrades) {menu = 1;}
                else if (source == bmenu_perm) {menu = 2;}
                else if (source == bmenu_relic) {menu = 3;}
                updatePanel(mainPanel);
            }
        };
        save.addActionListener(actionListener);
        respec.addActionListener(actionListener);
        reset.addActionListener(actionListener);

        bmenu_home.addActionListener(actionListener);
        bmenu_upgrades.addActionListener(actionListener);
        bmenu_perm.addActionListener(actionListener);
        bmenu_relic.addActionListener(actionListener);

        changeMenu(mainPanel, bread);
        updatePanel(mainPanel);

        // Components Added using Flow Layout
        bottomPanel.add(bmenu_home);
        bottomPanel.add(bmenu_upgrades);
        bottomPanel.add(bmenu_perm);
        bottomPanel.add(bmenu_relic);


        //Adding Components to the frame.
        getContentPane().add(BorderLayout.SOUTH, bottomPanel);
        getContentPane().add(BorderLayout.NORTH, mb);
        getContentPane().add(BorderLayout.CENTER, mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException {
        //Instantiate gui to initialize arraylists
        //Arraylists cannot be modified with a static method
        GUI gui = new GUI();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        //Set current bread type
        Bread current_bread = gui.pandesal;
        //Initialize ArrayLists
        gui.restoreProgress();


        //Refresh GUI every 100ms
        Runnable ui = new Runnable() {
            @Override
            public void run() {
                try {
                    gui.createWindow(current_bread);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        executor.scheduleWithFixedDelay(ui,0,100,TimeUnit.MILLISECONDS);
//        gui.createWindow(current_bread);
        //Add bread per second
        Runnable bps = new Runnable() {
            @Override
            public void run() {current_bread.addBread(Building.getTotal());
            }
        };
        executor.scheduleWithFixedDelay(bps,0,1,TimeUnit.SECONDS);
    }

}
