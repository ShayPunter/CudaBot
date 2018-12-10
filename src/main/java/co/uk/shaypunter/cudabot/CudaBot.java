package co.uk.shaypunter.cudabot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.requests.RequestFuture;
import co.uk.shaypunter.cudabot.command.DiscordMessageListenerSimpleCommandShellAdapter;
import co.uk.shaypunter.cudabot.commands.admin.FuckYouImAdmin;
import co.uk.shaypunter.cudabot.commands.fun.CardsAgainstHumanityCommand;
import co.uk.shaypunter.cudabot.commands.fun.DiceCommand;
import co.uk.shaypunter.cudabot.commands.fun.DoIHaveADrinkingProblem;
import co.uk.shaypunter.cudabot.commands.fun.WillEnglandWinCommand;
import co.uk.shaypunter.cudabot.commands.info.PingCommand;
import co.uk.shaypunter.cudabot.commands.moderation.PurgeAndLogCommand;
import co.uk.shaypunter.cudabot.commands.moderation.PurgeCommand;
import co.uk.shaypunter.cudabot.commands.moderation.PurgeUserCommand;
import co.uk.shaypunter.cudabot.commands.nsfw.BoobsCommand;
import co.uk.shaypunter.cudabot.level.LevellingMessageListener;
import uk.co.drcooke.commandapi.CommandAPI;
import uk.co.drcooke.commandapi.command.CommandShell;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CudaBot {

    private static CudaBot instance;
    private PrefixManager prefixManager;
    private CommandShell commandShell;
    private String ownerid;
    private JDA api;
    public ArrayList<Long> fyouimadminChannels = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        instance = new CudaBot();
        instance.startBot();
    }

    public void startBot() throws IOException {
        prefixManager = new PrefixManager();
        commandShell = CommandAPI.createSimpleCommandShell();
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = Files.readAllLines(Paths.get("config")).get(0);
        ownerid = Files.readAllLines(Paths.get("config")).get(1);
        builder.setToken(token);
        builder.addEventListener(new DiscordMessageListenerSimpleCommandShellAdapter(commandShell));
        builder.addEventListener(new LevellingMessageListener());
        registerCommands();
        try {
            api = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestFuture<PrivateChannel> channel = api.getGuildById(503312474460913690L).getMemberById(140605665772175361L).getUser().openPrivateChannel().submit();
                while(true) {
                    try {
                        channel.get().sendMessage("do your coursework u nob").submit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void registerCommands() {
        commandShell.register(new PingCommand());
        commandShell.register(new DiceCommand());
        commandShell.register(new PurgeCommand());
        commandShell.register(new PurgeUserCommand());
        commandShell.register(new CardsAgainstHumanityCommand());
        commandShell.register(new BoobsCommand());
        commandShell.register(new PurgeAndLogCommand());
        commandShell.register(new FuckYouImAdmin());
        commandShell.register(new WillEnglandWinCommand());
        commandShell.register(new DoIHaveADrinkingProblem());
    }

    public static CudaBot getInstance() {
        return instance;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public CommandShell getCommandShell() {
        return commandShell;
    }

    public String getOwnerid() {
        return ownerid;
    }
}
