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

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import uk.co.drcooke.commandapi.security.User;

public class DiscordUser implements User {

    private final Member member;
    private final MessageChannel channel;
    private final Guild guild;

    public DiscordUser(Member member, MessageChannel channel, Guild guild) {
        this.member = member;
        this.channel = channel;
        this.guild = guild;
    }

    @Override
    public String getName() {
        return member.getUser().getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return permission.equals("") || getMember().hasPermission(Permission.valueOf(permission));
    }

    public Member getMember() {
        return member;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }
}
