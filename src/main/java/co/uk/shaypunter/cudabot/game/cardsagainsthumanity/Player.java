package co.uk.shaypunter.cudabot.game.cardsagainsthumanity;

import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private int points;
    private List<String> possibleCards;
    private List<String> cards;
    private User user;

    public Player(User user, List<String> cards) {
        this.user = user;
        this.points = 0;
        this.possibleCards = cards;
        Random random = new Random();
        this.cards = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            cards.add(possibleCards.get(random.nextInt(possibleCards.size())));
        }
    }

    public int getPoints() {
        return points;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addCards(int amount){
        Random random = new Random();
        for(int i = 0; i < amount; i++) {
            cards.add(possibleCards.get(random.nextInt(possibleCards.size())));
        }
    }

    public User getUser() {
        return user;
    }
}
