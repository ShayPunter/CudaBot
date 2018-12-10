package co.uk.shaypunter.cudabot.game.cardsagainsthumanity;

public enum GamePhase {

    STARTING(120), PLAYERS(90), CZAR(90);

    private int timelimit;

    GamePhase(int timelimit) {
        this.timelimit = timelimit;
    }

    public int getTimelimit() {
        return timelimit;
    }
}
