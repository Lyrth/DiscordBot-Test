package lyr.testbot.objects;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.MultipartRequest;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.InputStream;
import java.time.Instant;
import java.util.function.Consumer;

public class Reply extends MessageCreateSpec {
    
    public EmbedCreateSpec embed;
    
    Reply(){
        embed = new EmbedCreateSpec();
    }

    public static Reply empty(){
        return new Reply();
    }

    public static Reply with(String reply){
        return (Reply) new Reply().setContent(reply);
    }

    public static Reply format(String format, Object... args){
        return (Reply) new Reply().setContent(String.format(format, args));
    }

    public static Reply withFile(String fileName, InputStream file){
        return (Reply) new Reply().setFile(fileName, file);
    }

    public static Reply withEmbed(EmbedCreateSpec embed){
        return new Reply().setIntEmbed(embed);
    }

    public static Reply withEmbed(Consumer<EmbedCreateSpec> spec){
        EmbedCreateSpec mutatedSpec = new EmbedCreateSpec();
        spec.accept(mutatedSpec);
        return new Reply().setIntEmbed(mutatedSpec);
    }

    private Reply setIntEmbed(EmbedCreateSpec embed){
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

    @Override
    public MultipartRequest asRequest() {
        this.setEmbed(embed);
        return super.asRequest();
    }
}
