package FinalProject;

public abstract class Bread {
    private int bps;

    public abstract double calculateBPS();

    public int getBps() {
        return bps;
    }

    public Bread(int bps){
        this.bps = bps;
    }
}

