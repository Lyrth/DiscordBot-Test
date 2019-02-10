package lyr.testbot.util.pagination;

import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import lyr.testbot.objects.Reply;
import reactor.core.publisher.Mono;

import java.util.*;

public class Paginator {

    // <MessageID,pagobject>
    private static Map<Snowflake,PaginatedObject> pagMessages = Collections.synchronizedMap(new HashMap<>());

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<EmbedCreateSpec> pages){
        return paginate(channel, pages, "");
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<EmbedCreateSpec> pages, String pageMessage){
        return paginate(channel, pages, pageMessage, ButtonSet.DEFAULT);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<EmbedCreateSpec> pages, ButtonSet buttonSet){
        return paginate(channel, pages, "", buttonSet);
    }

    public static Mono<Message> paginate(Mono<MessageChannel> channel, List<EmbedCreateSpec> pages, String pageMessage, ButtonSet buttonSet){
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

    public static void onReact(ReactionAddEvent e){
        pagMessages.computeIfPresent(e.getMessageId(), (id,pag) -> {
            pag.onReact(e.getEmoji())
                .then(e.getMessage())  // TODO Remove reaction first?
                .flatMap(m -> m.removeReaction(e.getEmoji(),e.getUserId())) // TODO button isToggle
                .subscribe();  // TODO RETURN THIS MONO
            return pag;
        });
    }

}

