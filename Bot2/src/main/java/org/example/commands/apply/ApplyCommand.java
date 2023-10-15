package org.example.commands.apply;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.commands.Command;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ApplyCommand extends Command {
    public ApplyCommand() {
        super("apply");
    }

    @Override
    public void handleMessageCommand(MessageReceivedEvent event, String[] args) {
    }


    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        //TODO Apply Command
    }
}
