package FinalProject;

public class Store {
    private int upgrade;
    private int onceupgrade;
    private double bps;

    public double getBps() {
        return bps;
    }

    public int getOnceupgrade() {
        return onceupgrade;
    }

    public int getUpgrade() {
        return upgrade;
    }

    public Store(double bps,int onceupgrade, int upgrade){
        this.bps = bps;
        this.onceupgrade = onceupgrade;
        this.upgrade = upgrade;
    }
}
