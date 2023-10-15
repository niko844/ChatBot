package org.example.commands.mute;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class UnmuteButton extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Role muted = event.getGuild().getRolesByName("Muted", true).get(0);
        if (event.getComponentId().equals("unmute-button")) {
            Member memberPerm = event.getMember();

            if (memberPerm.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_ROLES) || memberPerm.isOwner())) {
                event.reply("You don't have permission!").setEphemeral(true).queue();
                return;
            }

            MessageEmbed content = event.getMessage().getEmbeds().get(0);
            String description = content.getDescription().split(" ")[0];

            String memberId = description.replaceAll("<", "").replaceAll(">", "").replaceAll("@", "");
            Member member = event.getGuild().getMemberById(memberId);

            if (member.getRoles().stream().anyMatch(role -> role.equals(muted))) {

                member.getGuild().removeRoleFromMember(member, muted).queue();
                event.getMessage().delete().queue();

                event.reply(member.getAsMention() + " has been unmuted!").setEphemeral(true).queue();
            }
        }
    }
}
