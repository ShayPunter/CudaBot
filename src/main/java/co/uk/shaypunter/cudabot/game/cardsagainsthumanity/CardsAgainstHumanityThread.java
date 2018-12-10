package co.uk.shaypunter.cudabot.game.cardsagainsthumanity;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import co.uk.shaypunter.cudabot.command.DiscordUser;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class CardsAgainstHumanityThread implements Runnable{

    private DiscordUser context;
    private GamePhase gamePhase;
    private User cardCzar;
    private CardsAgainstHumanityGame game;

    public CardsAgainstHumanityThread(DiscordUser context, CardsAgainstHumanityGame game) {
        this.context = context;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            Message message = context.getChannel().sendMessage("@ everyone Cards Against Humanity starting in **10 seconds**.").submit().get();
            int seconds = 0;
            long last = System.currentTimeMillis();
            while(seconds <= 10) {
                if(System.currentTimeMillis() - 1000 > last) {
                    last = System.currentTimeMillis();
                    message.editMessage("@ everyone Cards Against Humanity starting in **" + (10 - seconds) + " seconds**.").submit();
                    seconds++;
                }
            }
            message.editMessage("@ everyone Cards Against Humanity started.").submit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String blackCard = game.getBlackCards().get(new Random().nextInt(game.getBlackCards().size()));
        for (Player player : game.getPlayers()) {
            try {
                StringBuilder message = new StringBuilder("The black card is: " + blackCard + "\nYour white cards are:\n" +
                        "1)(I am doing Kegels right now.)\n" +
                        "2)10,000 Syrian refugees.\n" +
                        "3)100% Pure New Zealand.\n" +
                        "4)2 Girls 1 Cup.\n" +
                        "5)400 years of colonial atrocities.\n" +
                        "6)50 mg of Zoloft daily.\n" +
                        "7)50,000 volts straight to the nipples.\n" +
                        "Enter the number of the 1 card(s) you want to select");
                int index = 1;
                for(String card : player.getCards()){
                    System.out.println(card);
                    message.append(index).append(")").append(card).append("\n");
                }
                player.getUser().openPrivateChannel().submit().get().sendMessage(message).submit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        //Game loop
        while(true){
            if(GameManager.INSTANCE.getGame(context.getGuild().getIdLong()).getPlayers().size() == 0){
                context.getChannel().sendMessage("No players remaining, ending game.").submit();
                GameManager.INSTANCE.getGame(context.getGuild().getIdLong()).end();
                break;
            }
        }
    }
}
