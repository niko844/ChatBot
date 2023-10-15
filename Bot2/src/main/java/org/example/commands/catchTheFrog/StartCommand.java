package org.example.commands.catchTheFrog;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartCommand extends Command {
    private final String person = Emoji.fromUnicode("U+1F9BD").getFormatted();
    private final String jew = Emoji.fromUnicode("U+1F9D4").getFormatted();
    private final String frog = Emoji.fromUnicode("U+1F438").getFormatted();
    private final Random random = new Random();

    protected static Member member;


    public StartCommand() {
        super("catch");
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        String[][] field = new String[6][6];
        String command = event.getName();
        member = event.getMember();
        fillMatrix(field);

        if (command.equals("catch")) {
            StringBuilder description = new StringBuilder();
            for (String[] strings : field) {
                for (int j = 0; j < field.length; j++) {
                    description.append(" | ");
                    if (strings[j].equals("*")) {
                        description.append(jew);
                    } else {
                        description.append(strings[j]);
                    }
                    if (j == 5) {
                        description.append(" |");
                    }
                }
                description.append("\n");
            }
            EmbedBuilder embedBuilder = new EmbedBuilder(embedBuilder(frog, description.toString(), person, frog));
            event.replyEmbeds(embedBuilder.build()).addActionRow(
                            Button.success("up-button", "Up"),
                            Button.success("down-button", "Down"),
                            Button.success("left-button", "Left"),
                            Button.success("right-button", "Right"),
                            Button.danger("end-button", "End"))
                    .queue();
        }
    }

    private EmbedBuilder embedBuilder(String emoji, String description, String person, String frog) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Catch the frog " + emoji)
                .setDescription("```" + description + "```")
                .setFooter(person + " - this is you\n" +
                        frog + " - this is the frog\n" +
                        "Be careful! There is a hidden jew between the people.\nHe will kill you!\n" +
                        "Created: " + member.getId());

        return embedBuilder;
    }

    private void fillMatrix(String[][] field) {
        List<Integer> cords = getCords();
        int personX = cords.get(0);
        int personY = cords.get(1);

        int frogX = cords.get(2);
        int frogY = cords.get(3);

        int jewX = cords.get(4);
        int jewY = cords.get(5);
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field.length; col++) {
                field[row][col] = "*";
            }
        }
        field[personX][personY] = person;
        field[frogX][frogY] = frog;
        field[jewX][jewY] = jew;
    }

    private List<Integer> getCords() {
        List<Integer> cords = new ArrayList<>();
        int personX = random.nextInt(3);
        int personY = random.nextInt(6);

        int frogX = random.nextInt(3);
        int frogY = random.nextInt(6);

        int jewX = random.nextInt(6);
        int jewY = random.nextInt(6);

        if (personY == frogY) {
            frogY = random.nextInt(6);
        }
        if (personX == frogX) {
            frogX = random.nextInt(3);
        }
        if (jewX == personX) {
            jewX = random.nextInt(6);
        }
        if (personY == jewY) {
            jewY = random.nextInt(6);
        }
        if (jewX == frogX) {
            frogX = random.nextInt(3);
        }
        if (jewY == frogY) {
            frogY = random.nextInt(6);
        }
        cords.add(personX);
        cords.add(personY);
        cords.add(frogX + 2);
        cords.add(frogY);
        cords.add(jewX);
        cords.add(jewY);
        return cords;
    }

    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {
    }
}
