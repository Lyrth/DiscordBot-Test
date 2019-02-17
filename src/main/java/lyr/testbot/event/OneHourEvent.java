package lyr.testbot.event;

public class OneHourEvent {

    private long tick;

    public OneHourEvent(long tick){
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }
}
