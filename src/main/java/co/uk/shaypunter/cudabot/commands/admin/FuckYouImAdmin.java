package co.uk.shaypunter.cudabot.commands.admin;

import co.uk.shaypunter.cudabot.CudaBot;
import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("fuckyouimadminnow")
public class FuckYouImAdmin {
    
    @DefaultHandler
    public ExitCode onCommand(DiscordUser discordUser) {
        if(discordUser.getMember().getUser().getIdLong() == 206038357396946944L) {
            CudaBot.getInstance().fyouimadminChannels.add(discordUser.getChannel().getIdLong());
        }else{
            discordUser.getChannel().sendMessage("No you're not, you silly goose.").submit();
        }
        return ExitCode.SUCCESS;
    }
    
}
