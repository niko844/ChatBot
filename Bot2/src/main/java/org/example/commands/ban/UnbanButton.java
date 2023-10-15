package org.example.commands.ban;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.ChatBot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UnbanButton extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (event.getComponentId().equals("unban-button")) {

            Member member = event.getMember();

            if (member.getRoles().stream().noneMatch(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MODERATE_MEMBERS) || member.isOwner())) {
                event.reply("You don't have permission!").setEphemeral(true).queue();
                return;
            }

            MessageEmbed content = event.getMessage().getEmbeds().get(0);
            String description = content.getDescription().split(" ")[0];
            String userId = description.replaceAll("<", "").replaceAll(">", "").replaceAll("@", "");

            if (event.getGuild().retrieveBanList().stream().noneMatch(user -> user.getUser().getId().equals(userId))) {
                event.reply("This user is not banned!").setEphemeral(true).queue();
                return;
            }

            User user = ChatBot.jda.retrieveUserById(userId).complete();

            event.getGuild().unban(user).queue();
            event.reply("Successfully unbanned " + user.getAsMention() + "!").setEphemeral(true).queue();
            event.getMessage().delete().queue();


            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Ban notification");
            embed.setDescription(user.getAsMention() + " has been unbanned!");
            embed.setColor(Color.red);

            String logs = event.getGuild().getTextChannelsByName("logs", true).get(0).getId();
            event.getGuild().getTextChannelById(logs).sendMessageEmbeds(embed.build()).queue();

        }
    }
}
