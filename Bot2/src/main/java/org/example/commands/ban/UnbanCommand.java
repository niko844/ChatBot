package org.example.commands.ban;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.ChatBot;
import org.example.commands.Command;

public class UnbanCommand extends Command {
    public UnbanCommand() {
        super("unban");
    }

    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MODERATE_MEMBERS) || member.isOwner())) {
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        String userId = event.getOption("userid").getAsString();
        if (event.getGuild().retrieveBanList().stream().noneMatch(user -> user.getUser().getId().equals(userId))){
            event.reply("This user is not banned!").setEphemeral(true).queue();
            return;
        }
        String logs = event.getGuild().getTextChannelsByName("logs", true).get(0).getId();

        User user = ChatBot.jda.retrieveUserById(userId).complete();
        event.getGuild().unban(user).queue();
        event.reply("Successfully unbanned " + user.getAsMention() + "!").setEphemeral(true).queue();
        event.getGuild().getTextChannelById(logs).sendMessage(user.getAsMention() + " was unbanned!").queue();

    }
}
