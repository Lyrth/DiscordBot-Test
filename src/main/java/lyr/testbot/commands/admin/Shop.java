package lyr.testbot.commands.admin;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Embed;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import lyr.testbot.util.pagination.ButtonSet;
import lyr.testbot.util.pagination.PaginatedObject;
import lyr.testbot.util.pagination.Paginator;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@CommandInfo(
    type = CommandType.ADMIN,
    desc = "Show some shop."
)
public class Shop extends Command {

    public Mono<Reply> execute(CommandObject command){

        List<Embed> pages = new ArrayList<>();
        Embed base = Embed
            .withTitle("Belethor's General Goods")
            .setColor(Color.decode("0x723232"))
            .setUrl("https://elderscrolls.fandom.com/wiki/Belethor")
            .setDescription(
                "Everything's for sale, my friend. Everything. If I had a sister, I'd sell her in a second.\n" +
                "[Google Docs Link](https://elderscrolls.fandom.com/wiki/Belethor)")
            .setFooter(
                "React to numbers to pick items.\n" +
                "Use ⏪ and ⏩ to navigate between pages.\n" +
                "\uD83D\uDD0D to search for an item.\n" +
                "❌ to exit the menu.",
                null);

        pages.add(base.clone()
            .addField(":one: - Cast Iron - 50C", "All you want for your smacking needs.\nEquipped - Applies +2 Strength.\n Used - Deals 2 damage and 1/6 chance of inflicting Paralyzed.")
            .addField(":two: - Dwarven Helmet - 200C", "Get that steampunk feels for yourself.\nEquipped - Applies +4 Strength, -1d3 Charisma.")
            .addField(":three: - Potato - 3C", "Potato.\nConsumed - Restores 3HP.\nThrown - Deals 1 damage and inflicts Confused for one turn.\nEquipped - Applies +4 Charisma.")

        );
        pages.add(base.clone()
            .addField(":one: - Fruit Juice - 7C", "Known for its medicinal properties.\nConsumed - Restores 2HP and applies Strengthened for two turns.")
            .addField(":two: - Milk - 10C", "Hey! It's milk time! Who doesn't want any of this?\nConsumed - Restores 4HP and applies Strengthened for three turns.")
            .addField(":three: - Banana - 5C", "It's ban(ana) time right now, folks!\nConsumed - Restores 3HP and applies Strengthened for two turns.\nUsed - Shoots banana seeds that deals 1 damage.")
        );
        pages.add(base.clone()
            .addField(":one: - Sister - 13370000C", "Everything's for sale, my friend. Everything.")
        );

        return Paginator.paginate(command.getChannel(),pages, ButtonSet.PAGE_NAV.append("mag").append(ButtonSet.NUM_TEN.getButtons()).append("x"))
            .doOnNext(m -> Paginator.editPaginatedObject(m.getId(), pag -> {
                pag.setOnCancel(msg -> msg.edit(spec -> spec.setEmbed(
                    base.clone()
                        .setDescription("Do come back.\n" +
                        "[Google Docs Link](https://elderscrolls.fandom.com/wiki/Belethor)")
                        .setFooter("",null))));
                pag.setAction("x",message -> pag.cancel().thenReturn(message));
                setActionsNum(pag);
            }))
            .map(m -> Reply.empty());
    }

    public void setActionsNum(PaginatedObject pag){
        Function<Message,Mono<Message>> func = msg -> createItemDesc(msg.getChannel());
        pag.setAction("one",func);
        pag.setAction("two",func);
        pag.setAction("three",func);
    }

    public Mono<Message> createItemDesc(Mono<MessageChannel> channel){
        List<Embed> page = new ArrayList<>();
        Embed emb = Embed
            .withTitle("Belethor's General Goods")
            .setColor(Color.decode("0x723232"))
            .setUrl("https://elderscrolls.fandom.com/wiki/Belethor")
            .setDescription(
                "Are you sure you want to buy **Potato** for 3 Credits?")
            .setFooter("",null);
        page.add(emb);
        return Paginator.paginate(channel,page,ButtonSet.YES_NO)
            .doOnNext(m -> Paginator.editPaginatedObject(m.getId(), pag -> {
                pag.setAction("white_check_mark",msg -> pag.cancel().then(msg.edit(spec -> spec.setEmbed(emb.clone().setDescription("_**Lyrthras** has bought **Potato** for 3 Credits._")))));
                pag.setAction("x",msg -> pag.cancel().then(msg.delete()).thenReturn(msg));
            }));
    }
}
