package org.example.commands.ban;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.commands.Command;

import java.awt.*;

public class BanCommand extends Command {
    public BanCommand(){
        super("ban");
    }

    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {

    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {

        Member member = event.getMember();
        Member interactionUser = event.getOption("user").getAsMember();
        String reason = event.getOption("reason").getAsString();
        int delays = event.getOption("bandelay").getAsInt();

        if (interactionUser.getRoles().stream().anyMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.BAN_MEMBERS))) {
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.BAN_MEMBERS) || member.isOwner())) {
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        event.getGuild().ban(interactionUser,delays,reason).queue();
        event.reply("Successfully banned " + interactionUser.getAsMention() + "!").setEphemeral(true).queue();

        String logs = event.getGuild().getTextChannelsByName("logs", true).get(0).getId();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Ban notification");
        embed.setDescription(interactionUser.getAsMention() + " has been banned!\nReason: " + reason);
        embed.setColor(Color.red);

        event.getGuild().getTextChannelById(logs).sendMessageEmbeds(embed.build())
                        .setActionRow(Button.danger("unban-button", "Unban")).queue();
    }
}
