package lyr.testbot.util.pagination;

import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.objects.builder.Embed;
import reactor.core.publisher.Mono;

import java.util.*;

public class Paginator {

    // <MessageID,pagobject>
    private static Map<Snowflake,PaginatedObject> pagMessages = Collections.synchronizedMap(new HashMap<>());

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
            .doOnNext(m -> pagMessages.put(m.getId(), new PaginatedObject(m, pages, pageMessage, buttonSet)))
            .publish(buttonSet::reactTo);
    }

    public static void onReactRemove(ReactionRemoveEvent e){
        pagMessages.computeIfPresent(e.getMessageId(), (id,pag) -> {
            Mono.just(e.getEmoji())
                .filter(pag::isToggle)
                .doOnNext(r -> pag.setToggleState(r,false))
                .flatMap(pag::onReact)
                .subscribe();
            return pag;
        });
    }

    public static void onReact(ReactionAddEvent e){
        pagMessages.computeIfPresent(e.getMessageId(), (id,pag) -> {
            Mono.when(
                e.getMessage()
                    .filter(m -> !pag.isToggle(e.getEmoji()))
                    .flatMap(m -> m.removeReaction(e.getEmoji(),e.getUserId())),
                Mono.just(e.getEmoji())
                    .doOnNext(r -> {
                        if (pag.isToggle(r)) pag.setToggleState(r,true);
                    })
                    .flatMap(pag::onReact)
            ).subscribe();  // TODO RETURN THIS MONO
            return pag;
        });
    }

}

