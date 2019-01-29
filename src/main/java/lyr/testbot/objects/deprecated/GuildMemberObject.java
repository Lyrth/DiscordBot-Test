package lyr.testbot.objects.deprecated;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.util.Fetch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Set;

public class GuildMemberObject extends UserObject {

    private Mono<Member> M;

    private Mono<Guild> guild;
    public Snowflake guildId;
    public String displayName;
    public Instant joinTime;

    private Flux<Role> roles;
    private Set<Snowflake> roleIds;

    private UserObject user;

    public GuildMemberObject(Mono<Member> member){
        M = member;
        member.subscribe(u -> {
            id = u.getId();
            username = u.getUsername();
            discriminator = u.getDiscriminator();
            mention = u.getMention();
            avatarUrl = Fetch.fetchAvatar(u);
            isBot = u.isBot();

            //roles = u.getRoles();
            displayName = u.getDisplayName();
            //guild = u.getGuild();  // fetch
            guildId = u.getGuildId();
            joinTime = u.getJoinTime();
            //roleIds = u.getRoleIds();
        });
    }



    public Snowflake getGuildId() {
        return guildId;
    }

    public String getDisplayName() {
        M.subscribe(u ->
            displayName = u.getDisplayName()
        );
        return displayName;
    }

    public Instant getJoinTime() {
        return joinTime;
    }

    public UserObject asUser(){
        //M.subscribe(u ->
        //    user = new UserObject(Mono.just(u))
        //);
        //return user;
        return new UserObject(M.flatMap(Mono::just));
    }

}
