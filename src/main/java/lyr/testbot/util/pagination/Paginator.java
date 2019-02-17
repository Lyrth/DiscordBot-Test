package lyr.testbot.util.pagination;

import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.objects.builder.Embed;
import lyr.testbot.objects.builder.Reply;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Paginator {

    public static final long CANCEL_DELAY = 30; // Seconds

    // <MessageID,pagobject>
    private static Map<Snowflake,PaginatedObject> pagMessages = Collections.synchronizedMap(new HashMap<>());

    private static Scheduler scheduler = Schedulers.single();

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages){
        return paginate(channel, pages, "");
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages, String pageMessage){
        return paginate(channel, pages, pageMessage, ButtonSet.DEFAULT);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages, ButtonSet buttonSet){
        return paginate(channel, pages, "", buttonSet);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages, String pageMessage, ButtonSet buttonSet){
        if (pages.size() < 1) return Mono.empty();
        //pagMessages.put(Snowflake.of(0),new PaginatedObject(null,pages,pageMessage,buttonSet));

        return channel.log("lyr.testbot.util.Log")
            .flatMap(chan ->
                chan.createMessage(
                    Reply.format(pageMessage,1,pages.size())
                    .setEmbed(pages.get(0))))
            .doOnNext(m ->
                pagMessages.put(m.getId(),
                    new PaginatedObject(m, pages, pageMessage, buttonSet, pag ->
                        Mono.delay(Duration.ofSeconds(CANCEL_DELAY))
                            .map(i -> pag)
                            .doOnNext(p -> pagMessages.remove(p.getId()))
                            .flatMap(PaginatedObject::cancel)
                            .subscribe()
                    )
                )
            )
            .publish(buttonSet::reactTo);
    }

    public static Mono<Void> onReactRemove(ReactionRemoveEvent e){
        if (pagMessages.containsKey(e.getMessageId())){
            final PaginatedObject pag = pagMessages.get(e.getMessageId());
            return Mono.just(e.getEmoji())
                .filter(pag::isToggle)
                .doOnNext(r -> pag.setToggleState(r,false))
                .flatMap(pag::onReact);
        } else {
            return Mono.empty();
        }
    }

    public static Mono<Void> onReact(ReactionAddEvent e){
        if (pagMessages.containsKey(e.getMessageId())){
            final PaginatedObject pag = pagMessages.get(e.getMessageId());
            return Mono.when(
                e.getMessage()
                    .filter(m -> !pag.isToggle(e.getEmoji()))
                    .flatMap(m -> m.removeReaction(e.getEmoji(),e.getUserId())),
                Mono.just(e.getEmoji())
                    .doOnNext(r -> {
                        if (pag.isToggle(r)) pag.setToggleState(r,true);
                    })
                    .flatMap(pag::onReact)
            );
        } else {
            return Mono.empty();
        }
    }

}

