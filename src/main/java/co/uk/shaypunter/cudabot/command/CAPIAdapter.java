package co.uk.shaypunter.cudabot.command;

import co.uk.shaypunter.cudabot.CudaBot;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import uk.co.drcooke.commandapi.command.CommandShell;
import uk.co.drcooke.commandapi.execution.ExitCode;

public class CAPIAdapter extends ListenerAdapter {

    private CommandShell commandShell;

    public CAPIAdapter(CommandShell shell) {
        this.commandShell = shell;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getChannelType() == ChannelType.TEXT) {
            String prefix = CudaBot.getInstance().getPrefixManager()
                    .getGuildPrefix(event.getGuild().getIdLong());
            if (event.getMessage().getContentRaw().startsWith(prefix)) {
                String input = event.getMessage().getContentRaw().substring(prefix.length());
                try {
                    if(commandShell.execute(input, new DiscordUser(event.getMember(), event.getTextChannel(), event.getGuild())) == ExitCode.NO_PERMISSION){
                        event.getTextChannel().sendMessage("No Permission.").submit();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    event.getTextChannel().sendMessage("Ooops! An error occurred. Please ask my creator to check the logs.").submit();
                }
            }
        }else if(event.getChannelType() == ChannelType.PRIVATE){
            String prefix = "~";
            if (event.getMessage().getContentRaw().startsWith(prefix)) {
                String input = event.getMessage().getContentRaw().substring(prefix.length());
                try{
                    if(commandShell.execute(input, new DiscordUser(event.getMember(), event.getPrivateChannel(), event.getGuild())) == ExitCode.NO_PERMISSION){
                        event.getPrivateChannel().sendMessage("No Permission.").submit();
                    }
                }catch (Exception e){
                    event.getTextChannel().sendMessage("Ooops! An error occurred. Please ask my creator to check the logs.").submit();
                }
            }
        }
    }

}
