package co.uk.shaypunter.cudabot.commands.fun;

import net.dv8tion.jda.core.entities.Role;
import co.uk.shaypunter.cudabot.command.DiscordUser;
import co.uk.shaypunter.cudabot.game.cardsagainsthumanity.CardsAgainstHumanityGame;
import co.uk.shaypunter.cudabot.game.cardsagainsthumanity.GameManager;
import uk.co.drcooke.commandapi.annotations.command.Command;
import uk.co.drcooke.commandapi.annotations.command.Subcommand;
import uk.co.drcooke.commandapi.execution.ExitCode;

@Command("cah")
public class CardsAgainstHumanityCommand {

    @Subcommand("create")
    public ExitCode createGame(DiscordUser user){

        return ExitCode.SUCCESS;
    }

    @Subcommand("join")
    public ExitCode joinGame(DiscordUser user){
        if(GameManager.INSTANCE.doesGameExist(user.getGuild().getIdLong())) {
            user.getChannel().sendMessage("Game joined.").submit();
            user.getGuild().getController().addRolesToMember(user.getMember(),
                    GameManager.INSTANCE.getGame(user.getGuild().getIdLong()).getRole()).submit();
            CardsAgainstHumanityGame game = GameManager.INSTANCE.getGame(user.getGuild().getIdLong());
            game.addPlayer(game.getPlayerFromUser(user.getMember().getUser()));
        }else{
            user.getChannel().sendMessage("No games have been created.").submit();
        }
        return ExitCode.SUCCESS;
    }

    @Subcommand("leave")
    public ExitCode leaveGame(DiscordUser user){
        if(GameManager.INSTANCE.doesGameExist(user.getGuild().getIdLong())) {
            CardsAgainstHumanityGame game = GameManager.INSTANCE.getGame(user.getGuild().getIdLong());
            if(game.getPlayers().contains(game.getPlayerFromUser(user.getMember().getUser()))) {
                user.getChannel().sendMessage("Game left.").submit();
                user.getGuild().getController().removeRolesFromMember(user.getMember(),
                        game.getRole()).submit();
                game.removePlayer(game.getPlayerFromUser(user.getMember().getUser()));
            }else{
                user.getChannel().sendMessage("You are not in a game.").submit();
            }
        }else{
            user.getChannel().sendMessage("No games have been created.").submit();
        }
        return ExitCode.SUCCESS;
    }

    @Subcommand("choose")
    public ExitCode chooseCard(DiscordUser user, int id){
        return ExitCode.SUCCESS;
    }

    @Subcommand("end")
    public ExitCode endGame(DiscordUser user){
        GameManager.INSTANCE.getGame(user.getGuild().getIdLong()).getRole().delete().submit();
        GameManager.INSTANCE.getGame(user.getGuild().getIdLong()).end();
        user.getChannel().sendMessage("Game ended.").submit();
        return ExitCode.SUCCESS;
    }

    @Subcommand("start")
    public ExitCode startGame(DiscordUser user){
        if(GameManager.INSTANCE.doesGameExist(user.getGuild().getIdLong())){
            user.getChannel().sendMessage("Game already exists.").submit();
        } else {
            user.getChannel().sendMessage("Game created.").submit();
            Role role = user.getGuild().getController().createRole().setName("CardsAgainstHumanity")
                    .setMentionable(true).complete();
            CardsAgainstHumanityGame game = new CardsAgainstHumanityGame(role, user);
            GameManager.INSTANCE.createGame(user.getGuild().getIdLong(), game);
        }
        if(GameManager.INSTANCE.doesGameExist(user.getGuild().getIdLong())){
            GameManager.INSTANCE.getGame(user.getGuild().getIdLong()).start();
        }else{
            user.getChannel().sendMessage("A game has not been created. Run c!cah create.").submit();
        }
        return ExitCode.SUCCESS;
    }

}
