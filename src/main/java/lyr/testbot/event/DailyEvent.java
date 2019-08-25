package lyr.testbot.event;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DailyEvent {

    private long tick;

    public static final ZoneId MIDNIGHT_REFERENCE = ZoneOffset.UTC;

    public DailyEvent(long tick){
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }

    public static Flux<DailyEvent> onThis(){
        ZonedDateTime now = ZonedDateTime.now(MIDNIGHT_REFERENCE);
        ZonedDateTime tom = now.toLocalDate().plusDays(1).atStartOfDay(MIDNIGHT_REFERENCE);
        Duration untilTomorrow = Duration.between(now, tom);
        return Flux.interval(
            untilTomorrow,
            Duration.ofDays(1)
        ).map(DailyEvent::new);
    }
}
