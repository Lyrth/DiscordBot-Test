package lyr.testbot.util.pagination;

import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import lyr.testbot.objects.builder.Embed;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

class PaginatedObject {
    private Message message;
    private List<Embed> pages;
    private String pageMessage;
    private ButtonSet buttonSet;

    private HashMap<ReactionEmoji, Function<Message, Mono<Message>>> actions = new HashMap<>();

    // keyExists : reaction is toggle ; value : toggle state, false: not pressed
    private HashMap<ReactionEmoji,Boolean> toggleReactions = new HashMap<>();

    private int page = 0;

    PaginatedObject(Message m, List<Embed> pages, String pageMessage, ButtonSet buttonSet){
        this.message = m;
        this.pages = pages;
        this.pageMessage = pageMessage;
        this.buttonSet = buttonSet;

        if (buttonSet == ButtonSet.PAGE_NAV){
            setAction("rewind", this::prev);
            setAction("fast_forward", this::next);
        }

    }

    public void addToggle(ReactionEmoji reaction){
        toggleReactions.put(reaction,false);
    }

    public boolean getToggleState(ReactionEmoji reaction){
        return toggleReactions.getOrDefault(reaction,false);
    }

    public void setToggleState(ReactionEmoji reaction, boolean state){
        toggleReactions.put(reaction,state);
    }

    public boolean isToggle(ReactionEmoji reactionEmoji){
        return toggleReactions.containsKey(reactionEmoji);
    }

    public Mono<Void> onReact(ReactionEmoji emoji){
        return actions.containsKey(emoji) ?
            Mono.from(actions.get(emoji).apply(message)).then() :
            Mono.empty();
    }

    public void setAction(String reaction, Function<Message,Mono<Message>> action){
        ReactionEmoji reactionEmoji = buttonSet.reaction(reaction);
        if (reactionEmoji == null) return;
        actions.put(reactionEmoji,action);
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
