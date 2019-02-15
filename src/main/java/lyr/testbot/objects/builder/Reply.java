package lyr.testbot.objects.builder;

import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.MessageCreateSpec;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.InputStream;
import java.time.Instant;
import java.util.function.Consumer;

public class Reply implements Consumer<MessageCreateSpec> {

    private String content;
    private Snowflake nonce;
    private boolean tts;
    private String fileName;
    private InputStream file;

    private Embed embed = Embed.empty();

    private boolean empty = false;

    Reply(){}
    Reply(boolean empty){
        this.empty = empty;
    }


    public static Reply empty(){
        return new Reply(true);
    }

    public static Reply with(String reply){
        return new Reply().setContent(reply);
    }

    public static Reply format(String format, Object... args){
        return new Reply().setContent(String.format(format, args));
    }

    public static Reply withFile(String fileName, InputStream file){
        return new Reply().setFile(fileName, file);
    }

    public static Reply withEmbed(Embed embed){
        return new Reply().setEmbed(embed);
    }

    public static Reply withEmbed(Consumer<Embed> spec){
        Embed mutatedSpec = Embed.empty();
        spec.accept(mutatedSpec);
        return new Reply().setEmbed(mutatedSpec);
    }

    public boolean isEmpty(){
        return embed.isEmpty() && empty;
    }

    public Reply setContent(String content) {
        this.content = content;
        return this;
    }

    public Reply setNonce(Snowflake nonce) {
        this.nonce = nonce;
        return this;
    }

    public Reply setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    // TODO: Add fileS
    public Reply setFile(String fileName, InputStream file) {
        this.fileName = fileName;
        this.file = file;
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

    public Reply setEmbedFooter(String text, @Nullable String iconUrl) {
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

    public Reply setEmbedAuthor(String name, @Nullable String url, @Nullable String iconUrl) {
        embed.setAuthor(name, url, iconUrl);
        return this;
    }

    public Reply addEmbedField(String name, String value, boolean inline) {
        embed.addField(name, value, inline);
        return this;
    }

    //// Helper functions
    // NotNull
    private boolean nn(Object o){
        return o != null;
    }

    // OnNotNull
    private <T> void onNN(T o, Consumer<T> c){
        if (o != null) c.accept(o);
    }

    @Override
    public void accept(MessageCreateSpec m) {
        if(isEmpty()) return;
        onNN(content, m::setContent);
        onNN(nonce, m::setNonce);
        m.setTts(tts);
        if(nn(fileName) && nn(file)) m.addFile(fileName,file);
        if(!embed.isEmpty()) m.setEmbed(embed);
    }
}
