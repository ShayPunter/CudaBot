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

import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Alias;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.annotations.security.Permission;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("purge")
@Alias({"clear", "collm"})
public class PurgeCommand {

    @DefaultHandler
    @Permission(permission="MESSAGE_MANAGE")
    public ExitCode onCommand(DiscordUser user, int amount){
        
        MessageHistory history = user.getChannel().getHistory();
        if(amount >= 100){
            int extra = amount % 100;
            amount -= extra;
            ((TextChannel) user.getChannel()).deleteMessages(history.retrievePast(extra).complete())
                    .submit();
            for(int i = 0; i < amount / 100; i++){
                ((TextChannel) user.getChannel()).deleteMessages(history.retrievePast(100).complete())
                        .submit();
            }
        }else {
            try {
                ((TextChannel) user.getChannel()).deleteMessages(history.retrievePast(amount).complete())
                        .submit();
            } catch (InsufficientPermissionException e) {
                user.getChannel().sendMessage("Bot doesnt have permissions").submit();
            }
        }
        return ExitCode.SUCCESS;
    }

}
