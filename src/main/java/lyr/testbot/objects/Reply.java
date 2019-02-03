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
    
    Reply(){}

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

    private void checkEmbed(){
        if (embed == null) embed = new EmbedCreateSpec();
    }

    private Reply setIntEmbed(EmbedCreateSpec embed){
        this.embed = embed;
        return this;
    }

    public Reply setEmbedTitle(String title){
        checkEmbed();
        embed.setTitle(title);
        return this;
    }

    public Reply setEmbedDescription(String description){
        checkEmbed();
        embed.setDescription(description);
        return this;
    }

    public Reply setEmbedUrl(String url){
        checkEmbed();
        embed.setUrl(url);
        return this;
    }

    public Reply setEmbedTimestamp(Instant timestamp){
        checkEmbed();
        embed.setTimestamp(timestamp);
        return this;
    }

    public Reply setEmbedColor(Color color){
        checkEmbed();
        embed.setColor(color);
        return this;
    }

    public Reply setEmbedFooter(String text, @Nullable String iconUrl) {
        checkEmbed();
        embed.setFooter(text, iconUrl);
        return this;
    }

    public Reply setEmbedImage(String url){
        checkEmbed();
        embed.setImage(url);
        return this;
    }

    public Reply setEmbedThumbnail(String url){
        checkEmbed();
        embed.setThumbnail(url);
        return this;
    }

    public Reply setEmbedAuthor(String name, @Nullable String url, @Nullable String iconUrl) {
        checkEmbed();
        embed.setAuthor(name, url, iconUrl);
        return this;
    }

    public Reply addEmbedField(String name, String value, boolean inline) {
        checkEmbed();
        embed.addField(name, value, inline);
        return this;
    }

    @Override
    public MultipartRequest asRequest() {
        if (embed != null) this.setEmbed(embed);
        return super.asRequest();
    }
}
