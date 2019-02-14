package lyr.testbot.objects.builder;

import discord4j.core.spec.EmbedCreateSpec;

import javax.annotation.Nullable;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Embed implements Consumer<EmbedCreateSpec> {

    String title;
    String description;
    String url;

    Instant timestamp;
    Color color;

    String footerText, footerIcon;
    String image;
    String thumbnail;
    String authorName, authorUrl, authorIcon;

    List<Field> fields;

    private boolean empty = false;

    static class Field {
        public final String name;
        public final String value;
        public final boolean inline;
        Field(String name, String value, boolean inline){
            this.name = name;
            this.value = value;
            this.inline = inline;
        }
    }

    private Embed(){}
    private Embed(boolean empty){
        this.empty = empty;
    }

    public static Embed empty(){
        return new Embed(true);
    }
    
    public static Embed withDesc(String description){
        return new Embed().setDescription(description);
    }

    public static Embed withTitle(String title){
        return new Embed().setTitle(title);
    }

    public Embed setTitle(String title) {
        empty = false;
        this.title = title;
        return this;
    }

    public Embed setDescription(String description) {
        empty = false;
        this.description = description;
        return this;
    }

    public Embed setUrl(String url) {
        empty = false;
        this.url = url;
        return this;
    }

    public Embed setTimestamp(Instant timestamp) {
        empty = false;
        this.timestamp = timestamp;
        return this;
    }

    public Embed setColor(Color color) {
        empty = false;
        this.color = color;
        return this;
    }

    public Embed setFooter(String footerText, @Nullable String iconUrl) {
        empty = false;
        this.footerText = footerText;
        this.footerIcon = iconUrl;
        return this;
    }

    public Embed setImage(String image) {
        empty = false;
        this.image = image;
        return this;
    }

    public Embed setThumbnail(String thumbnail) {
        empty = false;
        this.thumbnail = thumbnail;
        return this;
    }

    public Embed setAuthor(String name, @Nullable String url, @Nullable String iconUrl) {
        empty = false;
        this.authorName = name;
        this.authorUrl = url;
        this.authorIcon = iconUrl;
        return this;
    }

    public Embed addField(String name, String value, boolean inline){
        empty = false;
        if (fields == null)
            fields = new ArrayList<>();
        this.fields.add(new Field(name, value, inline));
        return this;
    }

    public boolean isEmpty(){
        return empty;
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
    public void accept(EmbedCreateSpec e) {
        if(empty) return;
        onNN(title, e::setTitle);
        onNN(description, e::setDescription);
        onNN(url, e::setUrl);
        onNN(timestamp, e::setTimestamp);
        onNN(color, e::setColor);
        if(nn(footerText)) e.setFooter(footerText,footerIcon);
        onNN(image, e::setImage);
        onNN(thumbnail, e::setThumbnail);
        if(nn(authorName)) e.setAuthor(authorName, authorUrl, authorIcon);
        onNN(fields, fis -> fis.forEach(f -> e.addField(f.name, f.value, f.inline)));
    }
}