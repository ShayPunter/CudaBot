package co.uk.shaypunter.cudabot.commands.fun;

import uk.co.drcooke.commandapi.annotations.command.Alias;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("noughtsandcrosses")
@Alias({"n+c", "nac", "ttt"})
public class NoughtsAndCrossesCommand {

    @DefaultHandler
    public ExitCode startGame(){return ExitCode.SUCCESS;}

}
