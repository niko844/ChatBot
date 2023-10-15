package org.example.commands.clear;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.commands.Command;

import java.util.List;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear");
    }

    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {
    }
    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Member member = event.getMember();
        if (command.equals("clear")) {

            if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_ROLES) || member.isOwner())) {
                event.reply("You don't have permission!").setEphemeral(true).queue();
                return;
            }

            List<Message> messageList = event.getChannel().getHistory().retrievePast(event.getOption("number").getAsInt()).complete();

            event.getChannel().purgeMessages(messageList);
            event.reply("Successfully deleted " + event.getOption("number").getAsInt() + " messages!").setEphemeral(true).queue();
        }
    }
}
