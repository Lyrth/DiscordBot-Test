package lyr.testbot.util.pagination;

import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class PaginatedObject {
    private Message message;
    private List<EmbedCreateSpec> pages;
    private String pageMessage;
    private ButtonSet buttonSet;

    private HashMap<ReactionEmoji, Function<Message, Mono<Message>>> actions = new HashMap<>();

    private int page = 0;



    PaginatedObject(Message m, List<EmbedCreateSpec> pages, String pageMessage, ButtonSet buttonSet){
        this.message = m;
        this.pages = pages;
        this.pageMessage = pageMessage;
        this.buttonSet = buttonSet;

        if (buttonSet == ButtonSet.PAGE_NAV){
            setAction("rewind", this::prev);
            setAction("fast_forward", this::next);
        }

    }

    public Mono<Void> onReact(ReactionEmoji emoji){
        return actions.containsKey(emoji) ?
            Mono.from(actions.get(emoji).apply(message)).then() :
            Mono.empty();
    }

    public void setAction(ReactionEmoji reaction, Consumer<Message> action){
        if (reaction == null) return;
        actions.put(reaction, m -> Mono.fromRunnable(() -> action.accept(m)).thenReturn(m));
    }

    public void setAction(String reaction, Consumer<Message> action){
        setAction(buttonSet.reaction(reaction),action);
    }

    public void setAction(ReactionEmoji reaction, Function<Message,Mono<Message>> action){
        if (reaction == null) return;
        actions.put(reaction,action);
    }

    public void setAction(String reaction, Function<Message,Mono<Message>> action){
        setAction(buttonSet.reaction(reaction),action);
    }

    public Mono<Message> prev(Message message){
        page--;
        if (page < 0) page = pages.size()-1;
        return message.edit(m -> m.setEmbed(pages.get(page)));
    }

    public Mono<Message> next(Message message){
        page++;
        if (page > pages.size()-1) page = 0;
        return message.edit(m -> m.setEmbed(pages.get(page)));
    }

}