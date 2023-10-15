package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.commands.CommandManager;
import org.example.commands.ban.UnbanButton;
import org.example.commands.catchTheFrog.ButtonEvent;
import org.example.commands.mute.UnmuteButton;


import javax.security.auth.login.LoginException;

import java.util.Arrays;
import java.util.List;


public class ChatBot {
    private final Dotenv config;

    public static JDA jda;

    public ChatBot() throws LoginException, InterruptedException {
        config = Dotenv.configure().directory("D:\\IntelliJ IDEA Community Edition 2022.2.2\\Bot2\\Bot2\\.env").load();
        String token = config.get("TOKEN");

        jda = JDABuilder.createDefault(token, getIntents())
                .addEventListeners(new CommandManager(), new UnmuteButton(), new ButtonEvent(), new UnbanButton())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("you sleep :}"))
                .enableCache(CacheFlag.ACTIVITY, CacheFlag.ONLINE_STATUS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

    }

    public Dotenv getConfig() {
        return config;
    }

    public static void main(String[] args) {
        try {
            ChatBot bot = new ChatBot();
        } catch (LoginException | InterruptedException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }

    private List<GatewayIntent> getIntents() {
        return Arrays.asList(GatewayIntent.values());
    }
}
