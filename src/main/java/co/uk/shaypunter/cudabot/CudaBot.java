package co.uk.shaypunter.cudabot;

import co.uk.shaypunter.cudabot.command.CAPIAdapter;
import co.uk.shaypunter.cudabot.command.PrefixManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import uk.co.drcooke.commandapi.CommandAPI;
import uk.co.drcooke.commandapi.command.CommandShell;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CudaBot {

    private static CudaBot instance;
    private PrefixManager prefixManager;
    private String ownerID = "";

    public static void main(String[] args) throws IOException {
        instance = new CudaBot();
        instance.init();
    }

    private void init() throws IOException {
        prefixManager = new PrefixManager();
        CommandShell commandShell = CommandAPI.createSimpleCommandShell();

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = Files.readAllLines(Paths.get("config")).get(0);
        ownerID = Files.readAllLines(Paths.get("config")).get(1);
        builder.setToken(token);
        builder.addEventListener(new CAPIAdapter(commandShell));

        try {
            builder.buildAsync();
        } catch (LoginException e) {
            System.err.println("System couldn't login.");
        }

        System.out.println("CudaBot has been initialised.");
    }

    public static CudaBot getInstance() {
        return instance;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public String getOwnerID() {
        return ownerID;
    }
}
