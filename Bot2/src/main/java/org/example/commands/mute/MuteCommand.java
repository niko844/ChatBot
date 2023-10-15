package org.example.commands.mute;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.commands.Command;

import java.awt.*;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute");
    }


    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {

    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Member member = event.getMember();
        Role muted = event.getGuild().getRolesByName("Muted", true).get(0);

        Member interactionUser = event.getOption("user").getAsMember();
        String reason = event.getOption("reason").getAsString();

        if (event.getUser().isBot()) {
            return;
        }
        if (command.equals("mute")) {

            if (interactionUser.getRoles().stream().anyMatch(role -> role.equals(muted))) {
                event.reply("This user is already muted!").setEphemeral(true).queue();
                return;
            }

            if (interactionUser.getRoles().stream().anyMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_ROLES))) {
                event.reply("You don't have permission!").setEphemeral(true).queue();
                return;
            }

            if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_ROLES) || member.isOwner())) {
                event.reply("You don't have permission!").setEphemeral(true).queue();
                return;
            }

            event.getGuild().addRoleToMember(interactionUser, muted).queue();

            String logs = event.getGuild().getTextChannelsByName("logs", true).get(0).getId();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Mute Notification");
            embed.setDescription(interactionUser.getAsMention() + " has been muted!\nReason: " + reason);
            embed.setColor(Color.red);
            event.reply(interactionUser.getAsMention() + " was muted successfully!").setEphemeral(true).queue();
            event.getGuild().getTextChannelById(logs).sendMessageEmbeds(embed.build()).setActionRow(Button.danger("unmute-button", "Unmute")
            ).queue();
        }
    }

}
