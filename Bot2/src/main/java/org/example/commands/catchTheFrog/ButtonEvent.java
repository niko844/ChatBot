package org.example.commands.catchTheFrog;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ButtonEvent extends ListenerAdapter {
    Random random = new Random();
    int personX;
    int personY;
    int frogX;
    int frogY;
    private final int jewX;
    private final int jewY;
    boolean won = true;

    private final String person = Emoji.fromUnicode("U+1F9BD").getFormatted();
    private final String jew = Emoji.fromUnicode("U+1F9D4").getFormatted();
    private final String frog = Emoji.fromUnicode("U+1F438").getFormatted();

    public ButtonEvent() {
        jewX = random.nextInt(6);
        jewY = random.nextInt(6);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {


        if (event.getComponentId().equals("up-button") || event.getComponentId().equals("down-button")
                || event.getComponentId().equals("left-button") || event.getComponentId().equals("right-button")
                || event.getComponentId().equals("end-button")) {

            if (!StartCommand.member.equals(event.getMember())) {
                event.reply("You cannot interact with this message!").setEphemeral(true).queue();
                return;
            }
            if (won) {
                String message = event.getMessage().getEmbeds().get(0).getDescription();
                String[][] field = new String[6][6];
                fillMatrix(field, message);
                getCords(field);
                moveFrog(field);
                movePerson(event, field);
                //TODO - disabled buttons
            } else if (event.getComponentId().equals("end-button")) {
                event.getMessage().delete().queue();
                event.reply("Hope you get back to catch some frogs :)").setEphemeral(true).queue();
                won = true;
            } else {
                event.reply("The game ended.").setEphemeral(true).queue();
            }
        }
    }


    private void moveFrog(String[][] field) {
        String button = getRandomEvent();
        if (button.equals("up-button")) {
            if (frogX - 1 >= 0) {
                frogX--;
                field[frogX][frogY] = Emoji.fromUnicode(frog).getFormatted();
                field[frogX + 1][frogY] = Emoji.fromUnicode(jew).getFormatted();
            }
        } else if (button.equals("down-button")) {
            if (frogX + 1 < 6) {
                frogX++;
                field[frogX][frogY] = Emoji.fromUnicode(frog).getFormatted();
                field[frogX - 1][frogY] = Emoji.fromUnicode(jew).getFormatted();

            }
        } else if (button.equals("right-button")) {
            if (frogY + 1 < 6) {
                frogY++;
                field[frogX][frogY] = Emoji.fromUnicode(frog).getFormatted();
                field[frogX][frogY - 1] = Emoji.fromUnicode(jew).getFormatted();
            }
        } else if (button.equals("left-button")) {
            if (frogY - 1 >= 0) {
                frogY--;
                field[frogX][frogY] = Emoji.fromUnicode(frog).getFormatted();
                field[frogX][frogY + 1] = Emoji.fromUnicode(jew).getFormatted();
            }
        }
    }

    private String getRandomEvent() {
        String event = "";
        int num = random.nextInt(4);
        switch (num) {
            case 0:
                event = "up-button";
                break;
            case 1:
                event = "down-button";
                break;
            case 2:
                event = "left-button";
                break;
            case 3:
                event = "right-button";
                break;
        }

        return event;
    }

    private void movePerson(@NotNull ButtonInteractionEvent event, String[][] field) {
        String description;
        if (event.getComponentId().equals("up-button")) {
            if (personX - 1 >= 0) {
                if (field[personX - 1][personY].equals(frog)) {
                    win(event);
                    return;
                } else if (personX - 1 == jewX && personY == jewY) {
                    lose(event);
                    return;
                } else if (checkForWinOrLose(event, field)) return;

                personX--;
                field[personX][personY] = Emoji.fromUnicode(person).getFormatted();
                field[personX + 1][personY] = Emoji.fromUnicode(jew).getFormatted();
                description = String.valueOf(embedBuilder(field));
                embedEdit(event, description);
            } else {
                event.reply("There is no space to move. Choose other direction!").setEphemeral(true).queue();
            }
        } else if (event.getComponentId().equals("down-button")) {
            if (personX + 1 < 6) {
                if (personX + 1 >= 0) {
                    if (field[personX + 1][personY].equals(frog)) {
                        win(event);
                        return;
                    } else if (personX + 1 == jewX && personY == jewY) {
                        lose(event);
                        return;
                    }
                } else if (checkForWinOrLose(event, field)) return;

                personX++;
                field[personX][personY] = Emoji.fromUnicode(person).getFormatted();
                field[personX - 1][personY] = Emoji.fromUnicode(jew).getFormatted();
                description = String.valueOf(embedBuilder(field));
                embedEdit(event, description);
            } else {
                event.reply("There is no space to move. Choose other direction!").setEphemeral(true).queue();
            }

        } else if (event.getComponentId().equals("right-button")) {
            if (personY + 1 < 6) {
                if (field[personX][personY + 1].equals(frog)) {
                    win(event);
                    return;
                } else if (personX == jewX && personY + 1 == jewY) {
                    lose(event);
                    return;
                } else if (checkForWinOrLose(event, field)) return;

                personY++;
                field[personX][personY] = Emoji.fromUnicode(person).getFormatted();
                field[personX][personY - 1] = Emoji.fromUnicode(jew).getFormatted();
                description = String.valueOf(embedBuilder(field));
                embedEdit(event, description);
            } else {
                event.reply("There is no space to move. Choose other direction!").setEphemeral(true).queue();

            }

        } else if (event.getComponentId().equals("left-button")) {
            if (personY - 1 >= 0) {
                if (field[personX][personY - 1].equals(frog)) {
                    win(event);
                    return;
                } else if (personX == jewX && personY - 1 == jewY) {
                    lose(event);
                    return;
                } else if (checkForWinOrLose(event, field)) return;

                personY--;
                field[personX][personY] = Emoji.fromUnicode(person).getFormatted();
                field[personX][personY + 1] = Emoji.fromUnicode(jew).getFormatted();
                description = String.valueOf(embedBuilder(field));
                embedEdit(event, description);
            } else {
                event.reply("There is no space to move. Choose other direction!").setEphemeral(true).queue();
            }
        } else if (event.getComponentId().equals("end-button")) {
            event.getMessage().delete().queue();
            event.reply("You did not managed to find the frog :(").setEphemeral(true).queue();
            won = true;
        }
    }

    private boolean checkForWinOrLose(@NotNull ButtonInteractionEvent event, String[][] field) {
        if (personY == frogY) {
            if (personX == frogX) {
                win(event);
                return true;
            }
        } else if (personY == jewY) {
            if (personX == jewX) {
                lose(event);
                return true;
            }
        }
        return false;
    }

    private void lose(@NotNull ButtonInteractionEvent event) {
        String description;
        description = "YOU WERE KILLED BY THE JEW!";
        event.editMessageEmbeds(embedBuilder(frog, description, person, frog).build()).complete();
        won = false;
    }

    private void win(@NotNull ButtonInteractionEvent event) {
        String description;
        description = "CONGRATS! You caught the frog!";
        event.editMessageEmbeds(embedBuilder(frog, description, person, frog).build()).complete();
        won = false;
    }

    private void embedEdit(@NotNull ButtonInteractionEvent event, String description) {
        event.editMessageEmbeds(embedBuilder(frog, description, person, frog).build()).queue();
    }

    private StringBuilder embedBuilder(String[][] field) {
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
        return description;
    }

    private EmbedBuilder embedBuilder(String emoji, String description, String person, String frog) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Catch the frog " + emoji)
                .setDescription("```" + description + "```")
                .setFooter(person + " - this is you\n" +
                        frog + " - this is the frog\n" +
                        "Be careful! There is a hidden jew between the people.\nHe will kill you!");

        return embedBuilder;
    }

    private void getCords(String[][] field) {
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field.length; col++) {
                if (field[row][col].equals(Emoji.fromUnicode("U+1F9BD").getFormatted())) {
                    personX = row;
                    personY = col;
                }
                if (field[row][col].equals(Emoji.fromUnicode("U+1F438").getFormatted())) {
                    frogX = row;
                    frogY = col;
                }
            }
        }
    }


    private void fillMatrix(String[][] matrix, String message) {
        String[] content = message.split("\\s*\\|\\s+|\\s*```\\s*\\n*");
        List<String> matrixContent = new ArrayList<>();
        for (String parts : content) {
            if (!parts.equals("")) {
                matrixContent.add(parts);
            }
        }
        int i = 0;
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                matrix[row][col] = matrixContent.get(i);
                i++;
            }
        }
    }
}