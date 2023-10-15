package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    private final String name;
    private final String[] aliases;
    private String usage;


    public Command(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void handleMessageCommand(MessageReceivedEvent event, String[] args);

    public abstract void handleSlashCommand(SlashCommandInteractionEvent event);

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }

}