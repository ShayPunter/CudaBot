package co.uk.shaypunter.cudabot.game.cardsagainsthumanity;

import java.util.concurrent.ConcurrentHashMap;

public enum GameManager {

    INSTANCE;

    private ConcurrentHashMap<Long, CardsAgainstHumanityGame> guildGameMap = new ConcurrentHashMap<>();

    public void createGame(long id, CardsAgainstHumanityGame game){
        guildGameMap.put(id, game);
    }

    public boolean doesGameExist(long id){
        return guildGameMap.containsKey(id);
    }

    public CardsAgainstHumanityGame getGame(long id){
        return guildGameMap.get(id);
    }

}
