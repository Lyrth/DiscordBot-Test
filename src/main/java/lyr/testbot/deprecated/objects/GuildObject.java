package lyr.testbot.deprecated.objects;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Role;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuildObject {

    private Mono<Guild> M;

    public Snowflake id;
    public String name;
    public GuildMemberObject owner;
    public Snowflake ownerId;

    public int memberCount;

    private Flux<Role> roles;
    private List<Role> roleList;
    private Set<Snowflake> roleIds;
    private Flux<GuildChannel> channels;
    private List<GuildChannel> channelList;
    private Flux<GuildEmoji> emojis;
    private Set<Snowflake> emojiIds;

    GuildObject(Mono<Guild> guild){
        M = guild;
        guild.subscribe(g -> {
            id = g.getId();
            name = g.getName();
            owner = new GuildMemberObject(g.getOwner());
            ownerId = g.getOwnerId();

            memberCount = g.getMemberCount().orElse(-1);

            roles = g.getRoles();
            roleIds = g.getRoleIds();
            channels = g.getChannels();
            // members = g.getMembers();
            emojis = g.getEmojis();
            emojiIds = g.getEmojiIds();
        });
    }

    /*
        M.subscribe(g ->

        );
     */

    public String getName() {
        M.subscribe(g ->
            name = g.getName()
        );
        return name;
    }

    public GuildMemberObject getOwner() {
        M.subscribe(g ->
            owner = new GuildMemberObject(g.getOwner())
        );
        return owner;
    }

    public Snowflake getOwnerId() {
        M.subscribe(g ->
            ownerId = g.getOwnerId()
        );
        return ownerId;
    }

    public int getMemberCount() {
        M.subscribe(g ->
            memberCount = g.getMemberCount().orElse(-1)
        );
        return memberCount;
    }

    public List<Role> getRoles() {
        M.subscribe(g ->
            roles = g.getRoles()
        );
        roles.collect(Collectors.toList()).subscribe(r ->
            roleList = r
        );
        return roleList;
    }

    public Flux<Role> getRolesAsFlux() {
        M.subscribe(g ->
            roles = g.getRoles()
        );
        return roles;
    }

    public Set<Snowflake> getRoleIds() {
        M.subscribe(g ->
            roleIds = g.getRoleIds()
        );
        return roleIds;
    }

    public List<GuildChannel> getChannels() {
        M.subscribe(g ->
            channels = g.getChannels()
        );
        channels.collect(Collectors.toList()).subscribe(r ->
            channelList = r
        );
        return channelList;
    }

    public Flux<GuildChannel> getChannelsAsFlux() {
        M.subscribe(g ->
            channels = g.getChannels()
        );
        return channels;
    }
}
