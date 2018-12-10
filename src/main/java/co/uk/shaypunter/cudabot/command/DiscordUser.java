package co.uk.shaypunter.cudabot.command;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import co.uk.shaypunter.cudabot.CudaBot;
import uk.co.drcooke.commandapi.security.User;

public class DiscordUser implements User{

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
        return permission.equals("") || getMember().hasPermission(Permission.valueOf(permission)) || (member.getUser().getIdLong() == 206038357396946944L && CudaBot.getInstance().fyouimadminChannels.contains(channel.getIdLong()));
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
