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

package co.uk.shaypunter.cudabot.commands.nsfw;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import co.uk.shaypunter.cudabot.command.DiscordUser;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.DefaultHandler;
import uk.co.drcooke.commandapi.execution.ExitCode;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

@Command("boobs")
public class BoobsCommand {

    @DefaultHandler
    public ExitCode onCommand(DiscordUser user){
        if(user.getChannel().getType() == ChannelType.TEXT){
            //if(((TextChannel)user.getChannel()).isNSFW()){
                //As of time of writing, this is how many pictures there are on oboobs.ru
                int id = new Random().nextInt(11576);
                try {
                    URL url = new URL("http://api.oboobs.ru/boobs/" + id);
                    Scanner scanner = new Scanner(url.openStream());
                    String data = scanner.nextLine();
                    System.out.println(data);
                    String[] items = data.split(", ");
                    String model = items[0].split(": ")[1].replaceAll("\"", "");
                    String imageLink = "http://media.oboobs.ru/" + items[1].split(": ")[1]
                            .replaceAll("\"", "");
                    System.out.println(items[1].split(": ")[1].split("/")[1]);
                    user.getChannel().sendFile(new URL(imageLink).openStream(),
                            items[1].split(": ")[1].split("/")[1].replace("[^0-9]", "") + ".jpg", new MessageBuilder().setContent("Model: " + model).build()).submit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //}else{
            //    user.getChannel().sendMessage("Run this command in a channel marked as nsfw.").submit();
            //}
        }else{
            user.getChannel().sendMessage("Run this command in a server.").submit();
        }
        return ExitCode.SUCCESS;
    }

}
