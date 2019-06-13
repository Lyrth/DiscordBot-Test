package lyr.testbot.objects.builder;

import discord4j.core.spec.EmbedCreateSpec;

import reactor.util.annotation.Nullable;
import java.awt.Color;
import java.time.Instant;
import java.util.function.Consumer;

public class Embed implements Consumer<EmbedCreateSpec> {

    private static final Consumer<EmbedCreateSpec> EMPTY = (spec -> {});

    private Consumer<EmbedCreateSpec> embedSpec;

    private Embed(Consumer<EmbedCreateSpec> embedSpec){
        this.embedSpec = embedSpec;
    }

    static Embed from(Consumer<EmbedCreateSpec> embedSpec){
        return new Embed(embedSpec);
    }

    public static Embed empty(){
        return new Embed(EMPTY);
    }

    public static Embed withDesc(String description){
        return new Embed(s -> s.setDescription(description));
    }

    public static Embed withTitle(String title){
        return new Embed(s -> s.setTitle(title));
    }

    public boolean isEmpty(){
        return embedSpec.equals(EMPTY);
    }

    public Embed setTitle(String title){
        embedSpec = embedSpec.andThen(s -> s.setTitle(title));
        return this;
    }

    public Embed setDescription(String description){
        embedSpec = embedSpec.andThen(s -> s.setDescription(description));
        return this;
    }

    public Embed setUrl(String url){
        embedSpec = embedSpec.andThen(s -> s.setUrl(url));
        return this;
    }

    public Embed setTimestamp(Instant timestamp){
        embedSpec = embedSpec.andThen(s -> s.setTimestamp(timestamp));
        return this;
    }

    public Embed setColor(Color color){
        embedSpec = embedSpec.andThen(s -> s.setColor(color));
        return this;
    }

    public Embed setFooter(String text, @Nullable String iconUrl){
        embedSpec = embedSpec.andThen(s -> s.setFooter(text, iconUrl));
        return this;
    }

    public Embed setImage(String url){
        embedSpec = embedSpec.andThen(s -> s.setImage(url));
        return this;
    }

    public Embed setThumbnail(String url){
        embedSpec = embedSpec.andThen(s -> s.setThumbnail(url));
        return this;
    }

    public Embed setAuthor(String name, @Nullable String url, @Nullable String iconUrl){
        embedSpec = embedSpec.andThen(s -> s.setAuthor(name, url, iconUrl));
        return this;
    }

    public Embed addField(String name, String value, boolean inline){
        embedSpec = embedSpec.andThen(s -> s.addField(name, value, inline));
        return this;
    }

    public Embed addField(String name, String value){
        embedSpec = embedSpec.andThen(s -> s.addField(name, value, false));
        return this;
    }

    @Override
    public void accept(EmbedCreateSpec embedCreateSpec) {
        embedSpec.accept(embedCreateSpec);
    }

    @Override
    public Embed clone(){
        return new Embed(embedSpec);
    }
}
