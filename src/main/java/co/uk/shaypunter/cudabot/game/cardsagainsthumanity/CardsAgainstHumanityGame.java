package co.uk.shaypunter.cudabot.game.cardsagainsthumanity;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import co.uk.shaypunter.cudabot.command.DiscordUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CardsAgainstHumanityGame {

    private Role role;
    private DiscordUser user;
    private List<Player> players;
    private List<String> whiteCards;
    private List<String> blackCards;
    private ConcurrentHashMap<User, Player> userPlayerMap = new ConcurrentHashMap<>();
    private boolean running;
    private Thread thread;

    public CardsAgainstHumanityGame(Role role, DiscordUser user) {
        this.role = role;
        this.user = user;
        this.players = new ArrayList<>();
        BufferedReader whiteCardReader = new BufferedReader(new InputStreamReader(getClass()
                .getResourceAsStream("/whitecards.txt")));
        whiteCards = whiteCardReader.lines().collect(Collectors.toList());
        BufferedReader blackCardReader = new BufferedReader(new InputStreamReader(getClass()
                .getResourceAsStream("/blackcards.txt")));
        blackCards = blackCardReader.lines().collect(Collectors.toList());
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public DiscordUser getUser() {
        return user;
    }

    public void setUser(DiscordUser user) {
        this.user = user;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player user){
        players.add(user);
    }

    public boolean isPlayer(Player user){
        return players.contains(user);
    }

    public void removePlayer(Player user){
        players.remove(user);
    }

    public Player getPlayerFromUser(User user){
        if(userPlayerMap.containsKey(user)){
            return userPlayerMap.get(user);
        }else{
            Player player = new Player(user, whiteCards);
            userPlayerMap.put(user, player);
            return player;
        }
    }

    public List<String> getWhiteCards() {
        return whiteCards;
    }

    public List<String> getBlackCards() {
        return blackCards;
    }

    public void start(){
        thread = new Thread(new CardsAgainstHumanityThread(user, this));
        thread.start();
    }

    public void end(){
        //Is this needed?
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
