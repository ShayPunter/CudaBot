package co.uk.shaypunter.cudabot.commands.fun;

import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("doihaveadrinkingproblem")
public class DoIHaveADrinkingProblem {
    
    @DefaultHandler
    public ExitCode onCommand(DiscordUser user){
        if(user.getMember().getUser().getIdLong() == 173165060565237761L){
            user.getChannel().sendMessage("Yes.").submit();
        }else{
            user.getChannel().sendMessage("No you silly goose.").submit();
        }
        return ExitCode.SUCCESS;
    }
    
}
