package org.example.commands.mute;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.commands.Command;

public class UnmuteCommand extends Command {
    MuteCommand mute = new MuteCommand();
    public UnmuteCommand() {
        super("unmute");
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

        if (interactionUser.getRoles().stream().anyMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_ROLES))){
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_ROLES) || member.isOwner())) {
            event.reply("You don't have permission!").setEphemeral(true).queue();
            return;
        }

        if (command.equals("unmute")){
            if (interactionUser.getRoles().stream().anyMatch(role -> role.equals(muted))){
                interactionUser.getGuild().removeRoleFromMember(interactionUser, muted).queue();

                event.reply(interactionUser.getAsMention() + " has been unmuted!").setEphemeral(true).queue();
            }
            else {
                event.reply("This user is not muted!").setEphemeral(true).queue();
            }
        }
    }
}
