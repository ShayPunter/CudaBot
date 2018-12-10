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

package co.uk.shaypunter.cudabot.commands.moderation;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Alias;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.annotations.security.Permission;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("purgeuser")
@Alias("clearuser")
public class PurgeUserCommand {

    @DefaultHandler
    @Permission(permission="MESSAGE_MANAGE")
    public ExitCode onCommand(DiscordUser user, String name, int amount){
        MessageHistory history = user.getChannel().getHistory();
        history.retrievePast(Math.min(100, amount)).queue(msgs -> {
            int deleted = 0;
            for(Message msg : msgs){
                if(msg.getMember().getUser().getName().equals(name)) {
                    if(deleted < amount) {
                        msg.delete().submit();
                        deleted++;
                    }
                }
            }
            user.getChannel().sendMessage("Deleted " + deleted + " messages. (Can only delete from the 100 most recent messages)").submit();
        });
        return ExitCode.SUCCESS;
    }

}
