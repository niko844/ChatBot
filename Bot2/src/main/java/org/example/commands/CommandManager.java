package org.example.commands;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.example.commands.apply.ApplyCommand;
import org.example.commands.ban.BanCommand;
import org.example.commands.ban.UnbanCommand;
import org.example.commands.catchTheFrog.StartCommand;
import org.example.commands.clear.ClearCommand;
import org.example.commands.kick.KickCommand;
import org.example.commands.mute.MuteCommand;
import org.example.commands.mute.UnmuteCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManager extends ListenerAdapter {
    private final List<Command> commands;

    public CommandManager() {
        this.commands = new ArrayList<>();
        this.commands.add(new KickCommand());
        this.commands.add(new UnbanCommand());
        this.commands.add(new BanCommand());
        this.commands.add(new MuteCommand());
        this.commands.add(new UnmuteCommand());
        this.commands.add(new StartCommand());
        this.commands.add(new ClearCommand());
        this.commands.add(new ApplyCommand());


    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Optional<Command> commandData = this.commands.stream().filter(x -> x.getName().equals(command)).findFirst();
        commandData.ifPresent(value -> value.handleSlashCommand(event));

    }

    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<SlashCommandData> commands = new ArrayList<>();

        OptionData userOption = new OptionData(OptionType.USER, "user","Provide user", true);
        OptionData reasonOption = new OptionData(OptionType.STRING, "reason", "Provide reason", true);
        OptionData userIdOption = new OptionData(OptionType.STRING, "userid", "Provide user id", true);
        OptionData banDelayOption = new OptionData(OptionType.INTEGER, "bandelay", "Provide ban delay", true);
        OptionData clearOption = new OptionData(OptionType.INTEGER, "number", "Provide number of messages", true);

        commands.add(Commands.slash("mute", "Mute @user reason").addOptions(userOption, reasonOption));
        commands.add(Commands.slash("ban", "Ban @user delay reason").addOptions(userOption,banDelayOption, reasonOption));
        commands.add(Commands.slash("unmute", "Unmute @user").addOptions(userOption));
        commands.add(Commands.slash("kick", "Kick @user reason").addOptions(userOption, reasonOption));
        commands.add(Commands.slash("unban", "Unban user").addOptions(userIdOption));
        commands.add(Commands.slash("catch", "Catch the frog"));
        commands.add(Commands.slash("clear", "Clear a given number of messages").addOptions(clearOption));
        commands.add(Commands.slash("apply", "Apply for staff"));


        event.getGuild().updateCommands().addCommands(commands).queue();
    }


}
