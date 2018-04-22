/*
 * Copyright 2018 David Cooke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
