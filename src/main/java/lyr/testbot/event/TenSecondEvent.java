package lyr.testbot.event;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class TenSecondEvent {

    private long tick;

    public TenSecondEvent(long tick){
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }

    public static Flux<TenSecondEvent> onThis(){
        return Flux.interval(Duration.ofSeconds(5),Duration.ofSeconds(10))
            .map(TenSecondEvent::new);
    }
}
