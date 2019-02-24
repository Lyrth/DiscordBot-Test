package lyr.testbot.objects.builder;

import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;

import javax.annotation.Nullable;
import java.awt.Color;
import java.io.InputStream;
import java.time.Instant;
import java.util.function.Consumer;

public class Reply implements Consumer<MessageCreateSpec> {

    private static final Consumer<MessageCreateSpec> EMPTY = (spec -> {});

    private Consumer<MessageCreateSpec> messageSpec;
    private Embed embed;

    private Reply(Consumer<MessageCreateSpec> messageSpec){
        this.messageSpec = messageSpec;
        this.embed = Embed.empty();
    }

    private Reply(Consumer<MessageCreateSpec> messageSpec, Embed embed){
        this.messageSpec = messageSpec;
        this.embed = embed;
    }

    private Reply(Consumer<MessageCreateSpec> messageSpec, Consumer<EmbedCreateSpec> embedSpec){
        this.messageSpec = messageSpec;
        this.embed = Embed.from(embedSpec);
    }

    public static Reply empty(){
        return new Reply(EMPTY);
    }

    public static Reply with(String reply){
        return new Reply(s -> s.setContent(reply));
    }

    public static Reply format(String format, Object... args){
        return new Reply(s -> s.setContent(String.format(format, args)));
    }

    public static Reply withFile(String fileName, InputStream file){
        return new Reply(s -> s.addFile(fileName, file));
    }

    public static Reply withEmbed(Embed embed){
        return new Reply(EMPTY, embed);
    }

    public static Reply withEmbed(Consumer<EmbedCreateSpec> embedSpec){
        return new Reply(EMPTY, embedSpec);
    }

    public boolean isEmpty(){
        return embed.isEmpty() && messageSpec.equals(EMPTY);
    }

    public Reply setContent(String content) {
        messageSpec.andThen(s -> s.setContent(content));
        return this;
    }

    public Reply setNonce(Snowflake nonce) {
        messageSpec.andThen(s -> s.setNonce(nonce));
        return this;
    }

    public Reply setTts(boolean tts) {
        messageSpec.andThen(s -> s.setTts(tts));
        return this;
    }

    public Reply addFile(String fileName, InputStream file) {
        messageSpec.andThen(s -> s.addFile(fileName, file));
        return this;
    }


    // EMBED FUNCTIONS

    public Reply setEmbed(Embed embed){
        this.embed = embed;
        return this;
    }

    public Reply setEmbedTitle(String title){
        embed.setTitle(title);
        return this;
    }

    public Reply setEmbedDescription(String description){
        embed.setDescription(description);
        return this;
    }

    public Reply setEmbedUrl(String url){
        embed.setUrl(url);
        return this;
    }

    public Reply setEmbedTimestamp(Instant timestamp){
        embed.setTimestamp(timestamp);
        return this;
    }

    public Reply setEmbedColor(Color color){
        embed.setColor(color);
        return this;
    }

    public Reply setEmbedFooter(String text, @Nullable String iconUrl){
        embed.setFooter(text, iconUrl);
        return this;
    }

    public Reply setEmbedImage(String url){
        embed.setImage(url);
        return this;
    }

    public Reply setEmbedThumbnail(String url){
        embed.setThumbnail(url);
        return this;
    }

    public Reply setEmbedAuthor(String name, @Nullable String url, @Nullable String iconUrl){
        embed.setAuthor(name, url, iconUrl);
        return this;
    }

    public Reply addEmbedField(String name, String value, boolean inline){
        embed.addField(name, value, inline);
        return this;
    }

    @Override
    public void accept(MessageCreateSpec messageCreateSpec) {
        if (embed.isEmpty()){
            messageSpec.accept(messageCreateSpec);
        } else {
            messageSpec.andThen(s -> s.setEmbed(embed)).accept(messageCreateSpec);
        }
    }
}
