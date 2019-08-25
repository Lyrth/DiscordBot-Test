package lyr.testbot.event;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class OneHourEvent {

    private long tick;

    public OneHourEvent(long tick){
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }

    public static Flux<OneHourEvent> onThis(){
        return Flux.interval(Duration.ofSeconds(5),Duration.ofHours(1))
            .map(OneHourEvent::new);
    }
}
