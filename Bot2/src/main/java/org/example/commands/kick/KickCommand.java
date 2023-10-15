package org.example.commands.kick;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.commands.Command;

import java.awt.*;


public class KickCommand extends Command {


    public KickCommand() {
        super("kick");
    }

    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {

    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Member member = event.getMember();
        Member interactionUser = event.getOption("user").getAsMember();
        String reason = event.getOption("reason").getAsString();

        if (interactionUser.getRoles().stream().anyMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.KICK_MEMBERS))) {
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.KICK_MEMBERS) || member.isOwner())) {
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        if (event.getUser().isBot()){
            return;
        }


        if (command.equals("kick")){
            event.getGuild().kick(interactionUser, reason).queue();
            event.reply("Successfully kicked " + interactionUser + "!").setEphemeral(true).queue();

            String logs = event.getGuild().getTextChannelsByName("logs", true).get(0).getId();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red);
            embed.setTitle("Kick notification");
            embed.setDescription(interactionUser.getAsMention() + " has been kicked!");
            event.getGuild().getTextChannelById(logs).sendMessageEmbeds(embed.build()).queue();
        }
    }
}
