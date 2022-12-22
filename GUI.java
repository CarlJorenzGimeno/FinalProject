package FinalProject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GUI extends JFrame {
    public static int menu=0;
    FileHandling fh = new FileHandling();
    Building clicker = new Building("Clicker",10,1,0,"Rapid Tap");
    Building baker = new Building("Baker",100,5,0,"Metal Rolling Pin");
    Building kitchen = new Building("Kitchen",1000,25,0,"Convection Oven");
    Building bakery = new Building("Bakery",10000,125,0,"More Staff");
    Building street = new Building("Store",100000,625,0,"Add Merchandise");
    Building town = new Building("Town",1000000,3125,0,"Community Service");
    Building city = new Building("City",10000000,15225,0,"City Act");

    static ArrayList<Building> buildings = new ArrayList<>();
    static ArrayList<Relic> relics = new ArrayList<>();


    private void restoreProgress() throws InvalidAlgorithmParameterException {
        //Initialize buildings array
        buildings.add(clicker);
        buildings.add(baker);
        buildings.add(kitchen);
        buildings.add(bakery);
        buildings.add(street);
        buildings.add(town);
        buildings.add(city);

        //Check if save exists
        if (fh.saveExists()){
            try {
                //Parse save and restore progress
                ArrayList<Object> save = fh.parseSave();
                ArrayList<Integer> upgrades = (ArrayList<Integer>) save.get(0);
                ArrayList<Boolean> perm = (ArrayList<Boolean>) save.get(1);
                Bread.setBread(upgrades.get(0));
                for (int i = 0; i < 7; i++) {
                    buildings.get(i).setNoOfBuildings(upgrades.get(i + 1));
                    buildings.get(i).setUpgraded(perm.get(i));
                }
                relics = fh.parseRelics();
            }
            catch(ClassCastException e){
                e.printStackTrace();
            }
        }
    }

    //Updates JPanel cleanly
    public void updatePanel(JPanel panel){
        panel.revalidate();
        panel.repaint();
    }

    //
    public void changeMenu(JPanel panel, Bread bread) throws IOException {
        //Buffer image
        BufferedImage breadImage = ImageIO.read(Objects.requireNonNull(GUI.class.getResource("pan.png")));
        JPanel picture = bread.getPanel(breadImage);

        //JPanel with box Layout. Group upgrades together vertically
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        //Bread clicking menu
        if (menu == 0) {
            panel.removeAll();
            panel.add(picture);
            panel.add(new JLabel(bread.getName()));
        }

        //Upgrades menu
        else if(menu == 1) {
            panel.removeAll();
            group.removeAll();
            group.add(new JLabel(bread.getBread()+" Bread"));
            for (Building building : buildings) {
                group.add(building.upgradePanel());
            }
            panel.add(group);
        }

        //Permanent Upgrades Menu
        else if (menu == 2) {
            panel.removeAll();
            group.removeAll();
            group.add(new JLabel(bread.getBread()+" Bread"));
            for (Building building : buildings) {
                group.add(building.permUpgradePanel());
            }
            panel.add(group);
        }

        //Relics Menu
        else if (menu == 3) {
            panel.removeAll();
            //Show placeholder text when relics list is empty
            if (relics.size() == 0){
                panel.add(new JLabel("<html><center>No Relics Yet.</center></html>"));
            }
            else{
                //Show Relic and their corresponding values
                JPanel relicSlot = new JPanel(new FlowLayout());
                for(Relic relic: relics){
                    relicSlot.add(new JLabel(relic.getRelic_name()));
                    relicSlot.add(new JLabel("+"+relic.getRelic_bps_value()+"% Bread Per Second"));
                    relicSlot.add(new JLabel("+"+relic.getRelic_click_value()+"% Bread Per Click"));
                }
                panel.add(relicSlot);
            }
        }
        updatePanel(panel);
    }


    public void createWindow(Bread bread) throws IOException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
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
        JMenuItem exit = new JMenuItem("Exit");
        option.add(save);
        option.add(respec);
        option.add(reset);
        option.add(exit);

        JMenu help = new JMenu("Help");
        JMenuItem help1 = new JMenuItem("Help");
        help.add(help1);
        mb.add(option);
        mb.add(help);

        //Create main panel to house the changing menu elements
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        //Creating the panel at bottom and adding components
        JPanel bottomPanel = new JPanel(); // the panel is not visible in output
        JButton bmenu_home = new JButton("Home");
        JButton bmenu_upgrades = new JButton("Upgrades");
        JButton bmenu_perm = new JButton("Perm. Upgrades");
        JButton bmenu_relic = new JButton("Relics");

        ActionListener actionListener = e -> {
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
                int choice = JOptionPane.showConfirmDialog(null,"Are you sure you wanna reset your progress for a new relic?","Respec",JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    Relic new_relic = Relic.createRelic(buildings);
                    if (new_relic != null) {
                        relics.add(new_relic);
                        Bread.setBread(0);
                        for (Building building : buildings) {
                            building.setNoOfBuildings(0);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient progress.");
                    }
                }
                else{JOptionPane.showMessageDialog(null,"Cancelled","Cancelled",JOptionPane.WARNING_MESSAGE);}
            }
            //Reset progress
            else if(source == reset){
                int choice = JOptionPane.showConfirmDialog(null,"This will reset all your progress until now.\nAre you sure you want to reset your progress?\n\nTHIS PROCESS CANNOT BE UNDONE.","Confirm Reset",JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION){
                    Bread.setBread(0);
                    for(Building building:buildings){building.setNoOfBuildings(0);}
                }
                else{
                    JOptionPane.showMessageDialog(null,"Cancelled","Cancelled",JOptionPane.WARNING_MESSAGE);
                }}
            //Save before exit
            else if(source == exit){
                int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?","Confirm Exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if (choice == JOptionPane.YES_OPTION){
                    try {
                        fh.composeSave(buildings,relics);
                        System.exit(0);
                    } catch (IOException | InvalidAlgorithmParameterException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Cancelled","Cancelled",JOptionPane.WARNING_MESSAGE);
                }
            }
            //Show help menu
            else if (source == help1){
                String message = "How to play\nClick, upgrade, click more, upgrade more.\n\nWhat does Respec do?\nRespec resets your playthrough but gives you relics to compensate.<br>These relics carry over when using respec.";
                JOptionPane.showMessageDialog(null,message,"Help",JOptionPane.INFORMATION_MESSAGE);
            }

            //Change main panel based on chosen option
            if (source == bmenu_home){menu = 0;}
            else if (source == bmenu_upgrades) {menu = 1;}
            else if (source == bmenu_perm) {menu = 2;}
            else if (source == bmenu_relic) {menu = 3;}
            updatePanel(mainPanel);
        };

        //Add action listeners
        save.addActionListener(actionListener);
        respec.addActionListener(actionListener);
        reset.addActionListener(actionListener);
        exit.addActionListener(actionListener);
        help1.addActionListener(actionListener);

        bmenu_home.addActionListener(actionListener);
        bmenu_upgrades.addActionListener(actionListener);
        bmenu_perm.addActionListener(actionListener);
        bmenu_relic.addActionListener(actionListener);

        changeMenu(mainPanel, bread);
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updatePanel(mainPanel);

        //Update main panel every 100 milliseconds
        Runnable ui = () -> {
            try {
                changeMenu(mainPanel, bread);
                updatePanel(mainPanel);
            } catch (IOException e) {throw new RuntimeException(e);}
        };
        executor.scheduleWithFixedDelay(ui,0,100,TimeUnit.MILLISECONDS);

        //Add bread per second
        Runnable bps = () -> bread.addBread(Building.getTotal());
        executor.scheduleWithFixedDelay(bps,0,1,TimeUnit.SECONDS);

        //Auto save every two minutes
        Runnable auto_save = () -> {
            try {fh.composeSave(buildings,relics);}
            catch (IOException | InvalidAlgorithmParameterException e) {throw new RuntimeException(e);}
        };
        executor.scheduleWithFixedDelay(auto_save,0,2,TimeUnit.MINUTES);

        // Components added to bottom panel
        bottomPanel.add(bmenu_home);
        bottomPanel.add(bmenu_upgrades);
        bottomPanel.add(bmenu_perm);
        bottomPanel.add(bmenu_relic);


        //Add Components to the frame.
        getContentPane().add(BorderLayout.SOUTH, bottomPanel);
        getContentPane().add(BorderLayout.NORTH, mb);
        getContentPane().add(BorderLayout.CENTER, mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException {
        //Instantiate gui to initialize arraylists
        //Arraylists cannot be modified with a static method
        GUI gui = new GUI();
        //Set current bread type
        Bread current_bread = new Bread(0);
        //Restore progress based on a save
        gui.restoreProgress();
        //Show window
        gui.createWindow(current_bread);
    }

}
