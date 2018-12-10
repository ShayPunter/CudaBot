package co.uk.shaypunter.cudabot.commands.fun;

import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Alias;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.execution.ExitCode;

import java.security.SecureRandom;
import java.util.Random;

@Command("dice")
@Alias("roll")
public class DiceCommand {

    private Random random;

    public DiceCommand(){
        random = new SecureRandom();
    }

    @DefaultHandler
    public ExitCode onCommand(DiscordUser user,int max){
        user.getChannel().sendMessage(String.valueOf(random.nextInt(max))).submit();
        return ExitCode.SUCCESS;
    }

}
