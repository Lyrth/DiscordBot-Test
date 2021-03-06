package lyr.testbot.util.pagination;

import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.rest.http.client.ClientException;
import lyr.testbot.objects.builder.Embed;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.util.FuncUtil;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Paginator {  // TODO: needs refactor!

    public static final long CANCEL_DELAY = 30; // Seconds

    // <MessageID,pagobject>
    private static ConcurrentHashMap<Snowflake,PaginatedObject> pagMessages = new ConcurrentHashMap<>();

    private static Scheduler scheduler = Schedulers.single();

    public static void editPaginatedObject(Snowflake messageId, Consumer<PaginatedObject> modifier){
        modifier.accept(pagMessages.get(messageId));
    }

    public static void removePaginatedObject(Snowflake messageId){
        pagMessages.remove(messageId);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages){
        return paginate(channel, pages, "");
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages, String messageContent){
        return paginate(channel, pages, messageContent, ButtonSet.DEFAULT);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages, ButtonSet buttonSet){
        return paginate(channel, pages, "", buttonSet);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<Embed> pages, String messageContent, ButtonSet buttonSet){
        if (pages.size() < 1) return Mono.empty();
        //pagMessages.put(Snowflake.of(0),new PaginatedObject(null,pages,pageMessage,buttonSet));

        return channel
            .flatMap(chan ->
                chan.createMessage(
                    Reply.format(messageContent,1,pages.size())
                    .setEmbed(pages.get(0))))
            .doOnNext(m ->
                pagMessages.put(m.getId(),
                    new PaginatedObject(m, pages, messageContent, buttonSet, pag ->
                        Mono.delay(Duration.ofSeconds(CANCEL_DELAY))
                            .thenReturn(pag)
                            .filter(p -> pagMessages.containsKey(p.getId()))
                            .doOnNext(p -> pagMessages.remove(p.getId()))
                            .flatMap(PaginatedObject::cancel)
                            .onErrorContinue(FuncUtil::noop)
                            .subscribe()
                    )
                )
            )
            .flatMap(buttonSet::reactTo);
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
            return e.getMessage()
                    .flatMap(m -> {
                        if (!pag.isToggle(e.getEmoji()))
                            return m.removeReaction(e.getEmoji(),e.getUserId());
                        else return Mono.just(m);
                    })
                    .thenReturn(e.getEmoji())
                    .doOnNext(r -> {
                        if (pag.isToggle(r)) pag.setToggleState(r,true);
                    })
                    .flatMap(pag::onReact)
                .onErrorResume(ClientException.class,
                    t -> Mono.just(">>> Paginator onReact error: ")
                        .map(s -> s + t.getMessage())
                        .doOnNext(Log::error)
                        .doOnNext(s -> t.printStackTrace())
                        .then()
                );
        } else {
            return Mono.empty();
        }
    }

}

