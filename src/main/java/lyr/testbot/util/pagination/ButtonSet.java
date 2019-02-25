package lyr.testbot.util.pagination;

import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import lyr.testbot.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

public enum ButtonSet {
    DIR_OK("arrow_up","arrow_down","arrow_left","arrow_right","ok"),
    PAGE_NAV("rewind","fast_forward"),
    PAGE_NAV_ADV("black_left_pointing_double_triangle_with_vertical_bar","rewind","fast_forward","black_right_pointing_double_triangle_with_vertical_bar","1234"),
    // "track_previous","play_pause","track_next","stop_button"
    PLAYER("black_left_pointing_double_triangle_with_vertical_bar","black_right_pointing_triangle_with_double_vertical_bar","black_right_pointing_double_triangle_with_vertical_bar","black_square_for_stop"),
    NUM_TEN("one","two","three","four","five","six","seven","eight","nine","keycap_ten"),
    NONE(),
    ;
    public static final ButtonSet DEFAULT = PAGE_NAV;

    private LinkedHashMap<String, ReactionEmoji> reactions;
    ButtonSet(String... buttons){
        this.customize(buttons);
    }

    public ButtonSet customize(String... buttons){
        reactions = new LinkedHashMap<>();
        for (String b : buttons) {
            reactions.put(b, StringUtil.getReactionEmoji(b));
        }
        return this;
    }

    public Mono<Message> reactTo(Message message){
        return message.removeAllReactions()
            .thenMany(Flux.fromIterable(reactions.values()))
            .flatMap(message::addReaction)
            .then(Mono.just(message));
    }

    public ReactionEmoji reaction(String emoji){
        return reactions.getOrDefault(emoji, null);
    }
}
