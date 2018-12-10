package co.uk.shaypunter.cudabot.command;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import co.uk.shaypunter.cudabot.CudaBot;
import uk.co.drcooke.commandapi.command.CommandShell;
import uk.co.drcooke.commandapi.execution.ExitCode;

public class DiscordMessageListenerSimpleCommandShellAdapter extends ListenerAdapter{

    private CommandShell commandShell;

    public DiscordMessageListenerSimpleCommandShellAdapter(CommandShell commandShell){
        this.commandShell = commandShell;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getChannelType() == ChannelType.TEXT) {
            if(event.getMember().getUser().isBot()) {
                return;
            }
            if(event.getMessage().getContentRaw().startsWith("spank")) {
                String userId = event.getMessage().getContentRaw().split(" ")[1];
                event.getMessage().getTextChannel().sendMessage(event.getMember().getUser().getName() + " spanked " + userId).submit();
            }
            if(event.getMessage().getContentRaw().startsWith("pipe")) {
                String userId = event.getMessage().getContentRaw().split(" ")[1];
                event.getMessage().getTextChannel().sendMessage(event.getMember().getUser().getName() + " piped " + userId).submit();
            }
            if(event.getMessage().getContentRaw().startsWith("slap")) {
                String userId = event.getMessage().getContentRaw().split(" ")[1];
                event.getMessage().getTextChannel().sendMessage(event.getMember().getUser().getName() + " slapped " + userId).submit();
            }
            if(event.getMember().getUser().getIdLong() == 206038357396946944L) {
                event.getTextChannel().sendMessage("i love maisha").submit();
            }
            if(event.getMember().getUser().getIdLong() == 453641495128309760L) {
                event.getTextChannel().sendMessage("i love dave").submit();
            }
            String prefix = CudaBot.getInstance().getPrefixManager()
                    .getGuildPrefix(event.getGuild().getIdLong());
            if (event.getMessage().getContentRaw().startsWith(prefix)) {
                String input = event.getMessage().getContentRaw().substring(prefix.length());
                try {
                    if(commandShell.execute(input, new DiscordUser(event.getMember(), event.getTextChannel(), event.getGuild())) == ExitCode.NO_PERMISSION){
                        event.getTextChannel().sendMessage("You do not have permission to run that command.").submit();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    event.getTextChannel().sendMessage("Something didn't work").submit();
                }
            }
        }else if(event.getChannelType() == ChannelType.PRIVATE){
            String prefix = "c!";
            if (event.getMessage().getContentRaw().startsWith(prefix)) {
                String input = event.getMessage().getContentRaw().substring(prefix.length());
                try{
                    if(commandShell.execute(input, new DiscordUser(event.getMember(), event.getPrivateChannel(), event.getGuild())) == ExitCode.NO_PERMISSION){
                        event.getPrivateChannel().sendMessage("You do not have permission to run that command.").submit();
                    }
                }catch (Exception e){
                    event.getTextChannel().sendMessage("Something didn't work").submit();
                }
            }
        }
    }

}
