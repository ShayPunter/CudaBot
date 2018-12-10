package co.uk.shaypunter.cudabot.level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum LevelManager {
    INSTANCE;

    private final Map<Long, Map<Long, Integer>> guildUserXPMap;

    LevelManager() {
        this.guildUserXPMap = new ConcurrentHashMap<>();
    }
}
