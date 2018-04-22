package co.uk.shaypunter.cudabot.command;

import java.util.HashMap;
import java.util.Map;

public class PrefixManager {

    private Map<Long, String> prefixMap;

    public PrefixManager() {
        this.prefixMap = new HashMap<>();
    }

    public void setGuildPrefix(long guild, String prefix){
        prefixMap.put(guild, prefix);
    }

    public String getGuildPrefix(long guild){
        return prefixMap.getOrDefault(guild, "~");
    }

}
