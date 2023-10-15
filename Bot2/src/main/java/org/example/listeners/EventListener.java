//package org.example.listeners;
//
//import net.dv8tion.jda.api.OnlineStatus;
//import net.dv8tion.jda.api.entities.*;
//import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
//import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
//import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.interactions.commands.OptionMapping;
//import net.dv8tion.jda.api.interactions.commands.OptionType;
//import net.dv8tion.jda.api.interactions.commands.build.CommandData;
//import net.dv8tion.jda.api.interactions.commands.build.Commands;
//import net.dv8tion.jda.api.interactions.commands.build.OptionData;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class EventListener extends ListenerAdapter {
//    @Override
//    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
//        String command = event.getName();
//        if (command.equals("welcome")) {
//            event.reply("Welcome " + event.getUser().getAsMention()).setEphemeral(true).queue();
//        } else if (command.equals("roles")) {
//            event.deferReply().setEphemeral(true).queue();
//            String response = "";
//            for (Role role : event.getGuild().getRoles()) {
//                response = response.concat(role.getAsMention() + "\n");
//            }
//            event.getHook().sendMessage(response).queue();
//        } else if (command.equals("say")) {
//            OptionMapping messageOption = event.getOption("message");
//            String message = messageOption.getAsString();
//
//            MessageChannel channel;
//            OptionMapping channelOption = event.getOption("channel");
//            if (channelOption != null) {
//                channel = channelOption.getAsChannel().asGuildMessageChannel();
//            } else {
//                channel = event.getChannel();
//            }
//
//            channel.sendMessage(message).queue();
//        } else if (command.equals("giverole")) {
//            //input
//            Member member = event.getOption("user").getAsMember();
//            Role role = event.getOption("role").getAsRole();
//
//            //output
//            event.getGuild().addRoleToMember(member, role).queue();
//            event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").setEphemeral(true).queue();
//
//
//        } else if (command.equals("removerole")) {
//            //input
//            Member member = event.getOption("user").getAsMember();
//            Role role = event.getOption("role").getAsRole();
//
//            //output
//            event.getGuild().removeRoleFromMember(member, role).queue();
//            event.reply(role.getAsMention() + " has been removed from " + member.getAsMention() + "!").setEphemeral(true).queue();
//
//        }
//    }
//
//    @Override
//    public void onGuildReady(@NotNull GuildReadyEvent event) {
//        List<CommandData> commandData = new ArrayList<>();
//        //welcome command ->
//        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
//        //roles command ->
//        commandData.add(Commands.slash("roles", "Display all roles on the server."));
//
//        //say command ->
//        OptionData messageOption = new OptionData(OptionType.STRING, "message", "The message you want the bot to say.", true);
//        OptionData channelOption = new OptionData(OptionType.CHANNEL, "channel", "Write the channel you want to send the message in.", false)
//                .setChannelTypes(ChannelType.TEXT, ChannelType.GUILD_PUBLIC_THREAD, ChannelType.NEWS);
//
//        commandData.add(Commands.slash("say", "Make the bot say a message.").addOptions(messageOption, channelOption));
//        //add role ->
//        OptionData userOption = new OptionData(OptionType.USER, "user", "Select user.", true);
//        OptionData roleOption = new OptionData(OptionType.ROLE, "role", "Select role.", true);
//        commandData.add(Commands.slash("giverole", "Give user a role").addOptions(userOption, roleOption));
//
//        //remove role ->
//        commandData.add(Commands.slash("removerole", "Remove user a role").addOptions(userOption, roleOption));
//
//
//        event.getGuild().updateCommands().addCommands(commandData).queue();
//    }
//}
