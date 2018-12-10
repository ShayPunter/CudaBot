package co.uk.shaypunter.cudabot.level;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Random;

public class LevellingMessageListener extends ListenerAdapter {

    private Random random = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContentRaw();
        int xp = (random.nextInt(15) + 10) * Math.abs((int)(Math.sin(System.currentTimeMillis())*3));
        System.out.println(xp);
    }

}
