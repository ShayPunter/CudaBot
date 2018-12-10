package co.uk.shaypunter.cudabot.commands.fun;

import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("willenglandwintheworldcup")
public class WillEnglandWinCommand {
    
    @DefaultHandler
    public ExitCode onCommand(DiscordUser discordUser){
        discordUser.getChannel().sendMessage("No you silly goose.").submit();
        return ExitCode.SUCCESS;
    }
    
}
