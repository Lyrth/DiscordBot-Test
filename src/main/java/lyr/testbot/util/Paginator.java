package lyr.testbot.util;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import lyr.testbot.objects.Reply;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Paginator {

    private static Map<Snowflake,PaginatedObject> pagMessages = new ConcurrentHashMap<>();

    public enum ButtonSet {
        DIR_OK("arrow_up","arrow_down","arrow_left","arrow_right","ok"),
        PAGE_NAV("rewind","fast_forward"),
        PAGE_NAV_ADV("black_left_pointing_double_triangle_with_vertical_bar","rewind","fast_forward","black_right_pointing_double_triangle_with_vertical_bar","1234"),
        // "track_previous","play_pause","track_next","stop_button"
        PLAYER("black_left_pointing_double_triangle_with_vertical_bar","black_right_pointing_triangle_with_double_vertical_bar","black_right_pointing_double_triangle_with_vertical_bar","black_square_for_stop"),
        NUM_TEN("one","two","three","four","five","six","seven","eight","nine","keycap_ten"),
        ;
        public static final ButtonSet DEFAULT = PAGE_NAV;

        private List<ReactionEmoji> reactions;
        ButtonSet(String... buttons){
            reactions = new ArrayList<>();
            for (String b : buttons) {
                reactions.add(
                    ReactionEmoji.unicode(StringUtil.getEmoji(b))
                );
            }
        }

        public Mono<Message> reactTo(Mono<Message> message){
            return Flux.fromIterable(reactions)
                .flatMap(r -> message.flatMap(m -> m.addReaction(r)))
                .then(message);
        }
    }

    public static Mono<Message> paginate(Mono<TextChannel> channel, List<EmbedCreateSpec> pages){
        return paginate(channel, pages, "");
    }

    public static Mono<Message> paginate(Mono<TextChannel> channel, List<EmbedCreateSpec> pages, String pageMessage){
        return paginate(channel, pages, pageMessage, ButtonSet.DEFAULT);
    }

    public static Mono<Message> paginate(Mono<TextChannel> channel, List<EmbedCreateSpec> pages, ButtonSet buttonSet){
        return paginate(channel, pages, "", buttonSet);
    }

    public static Mono<Message> paginate(Mono<TextChannel> channel, List<EmbedCreateSpec> pages, String pageMessage, ButtonSet buttonSet){
        if (pages.size() < 1) return Mono.empty();
        return channel
            .flatMap(chan ->
                chan.createMessage(
                    Reply.format(pageMessage,1,pages.size())
                    .setEmbed(pages.get(0))))
            .doOnNext(m -> pagMessages.put(m.getId(),new PaginatedObject(m,pages,pageMessage,buttonSet)))
            .publish(buttonSet::reactTo);
    }

}

class PaginatedObject {
    private Message message;
    private List<EmbedCreateSpec> pages;
    private String pageMessage;
    private Paginator.ButtonSet buttonSet;
    PaginatedObject(Message m, List<EmbedCreateSpec> pages, String pageMessage, Paginator.ButtonSet buttonSet){
        this.message = m;
        this.pages = pages;
        this.pageMessage = pageMessage;
        this.buttonSet = buttonSet;
    }

    public Message getMessage() {
        return message;
    }

    public List<EmbedCreateSpec> getPages() {
        return pages;
    }

    public String getPageMessage() {
        return pageMessage;
    }

    public Paginator.ButtonSet getButtonSet() {
        return buttonSet;
    }

}
