package lyr.testbot.deprecated.objects;

import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.util.Fetch;
import reactor.core.publisher.Mono;

public class UserObject {

    private Mono<User> M;

    public Snowflake id;
    public String username;
    public String discriminator;
    public String mention;
    public String avatarUrl;
    boolean isBot;

    UserObject(){}

    public UserObject(Mono<User> user){
        M = user;
        user.subscribe(u -> {
            id = u.getId();
            username = u.getUsername();
            discriminator = u.getDiscriminator();
            mention = u.getMention();
            avatarUrl = Fetch.fetchAvatar(u);
            isBot = u.isBot();
        });
    }

    public String getUsername(){
        M.subscribe(u ->
            username = u.getUsername()
        );
        return username;
    }

    public String getDiscriminator(){
        M.subscribe(u ->
            discriminator = u.getDiscriminator()
        );
        return discriminator;
    }

    public String getMention(){
        return mention;
    }

    public String getAvatarUrl(){
        M.subscribe(u ->
            avatarUrl = Fetch.fetchAvatar(u)
        );
        return avatarUrl;
    }

    public boolean isBot() {
        return isBot;
    }

    public GuildMemberObject asMember(Snowflake guildId){
        return new GuildMemberObject(M.flatMap(u -> u.asMember(guildId)));
    }

}

