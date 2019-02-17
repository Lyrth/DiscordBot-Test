package lyr.testbot.event;

public class TenSecondEvent {

    private long tick;

    public TenSecondEvent(long tick){
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }
}
