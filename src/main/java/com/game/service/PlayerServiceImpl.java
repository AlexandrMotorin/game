package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.NoSuchPlayerException;
import com.game.exceptions.NoValidPlayerException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player getPlayer(String id) {
        Long playerId = examinationValidId(id);
        return playerRepository.findById(playerId).orElseThrow( () ->
                new NoSuchPlayerException("Такой игрок не найден"));
    }

    @Override
    public List<Player> getAllPlayers(String name,
                                      String title,
                                      Race race,
                                      Profession profession,
                                      Long after,
                                      Long before,
                                      Boolean banned,
                                      Integer minExperience,
                                      Integer maxExperience,
                                      Integer minLevel,
                                      Integer maxLevel)
    {

        boolean bannedPlayer = banned != null && banned;

        List<Player> players = new ArrayList<>();
        playerRepository.findAll().forEach((player) -> {

            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && player.getRace() != race) return;
            if (profession != null && player.getProfession() != profession) return;
            if (banned != null && player.getBanned() != bannedPlayer) return;
            if (minExperience != null && player.getExperience() < minExperience) return;
            if (maxExperience != null && player.getExperience() > maxExperience) return;
            if (minLevel != null && player.getLevel() < minLevel) return;
            if (maxLevel != null && player.getLevel() > maxLevel) return;
            if (after != null && player.getBirthday().getTime() < after) return;
            if (before != null && player.getBirthday().getTime() > before) return;

            players.add(player);
        });

        return players;
    }

    public List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize) {
        final int page = pageNumber == null ? 0 : pageNumber;
        final int size = pageSize == null ? 3 : pageSize;
        final int from = page * size;
        int to = from + size;
        if (to > players.size()) to = players.size();
        return players.subList(from, to);
    }

    @Override
    public List<Player> sortPlayers(List<Player> players, PlayerOrder order) {

        if(order != null && order != PlayerOrder.ID){
            players.sort((p1,p2) -> {
                switch (order){
                    case NAME: p1.getName().compareTo(p2.getName());
                    case LEVEL: p1.getLevel().compareTo(p2.getLevel());
                    case BIRTHDAY: p1.getBirthday().compareTo(p2.getBirthday());
                    case EXPERIENCE: p1.getExperience().compareTo(p2.getExperience());
                    default: return 0;
                }

            });
        } else players.sort(Comparator.comparing(Player::getId));

        return players;
    }

    @Override
    public void deletePlayer(String id) {
        Player player = getPlayer(id);
        playerRepository.delete(player);
    }

    @Override
    public Player updatePlayer(String id, Player player) {

        Player source = getPlayer(id);

        if(invalidValues(player)) throw new NoValidPlayerException("Введены некоректные значения");

        if (player.getName() != null) source.setName(player.getName());
        if (player.getTitle() != null) source.setTitle(player.getTitle());
        if (player.getRace() != null) source.setRace(player.getRace());
        if (player.getProfession() != null) source.setProfession(player.getProfession());
        if (player.getBirthday() != null) source.setBirthday(player.getBirthday());
        if (player.getExperience() != null) source.setExperience(player.getExperience());
        if (player.getBanned() != null) source.setBanned(player.getBanned());


        lvlCalculation(source);

        playerRepository.save(source);

        return source;
    }

    @Override
    public Player addNewPlayer(Player player) {

        if (containsNullField(player) || invalidValues(player))
            throw new NoValidPlayerException("Введены некорректные значения");

        lvlCalculation(player);
        playerRepository.save(player);

        return player;
    }

    public Long examinationValidId(String id){
        long playerId;
        try {
            playerId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new NoValidPlayerException("Неверный формат id");
        }
        if(playerId <= 0) throw new NoValidPlayerException("Такой id не может существовать");
        return playerId;
    }

    public boolean invalidValues(Player player){
        String name = player.getName();
        String title = player.getTitle();
        Integer exp = player.getExperience();

        if (name!= null) {
            if(name.length()>12 || name.length()==0) return true;
        }
        if (title != null) {
            if(title.length() > 30) return true;
        }
        if (exp != null) {
            if(exp > 10000000 || exp < 0) return true;
        }
        if (player.getBirthday() != null) {
            if (player.getBirthday().getTime() < 0) return true;
            else{
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(player.getBirthday());
                int y = calendar.get(Calendar.YEAR);
                if (y < 2000 || y > 3000) return true;
            }
        }

        return  false;
    }

    public boolean containsNullField(Player player){
        return player.getName() == null
                || player.getTitle() == null
                || player.getExperience() == null
                || player.getRace() == null
                || player.getProfession() == null
                || player.getBirthday() == null;
    }

    public void lvlCalculation(Player player){
        Integer exp = player.getExperience();
        Integer lvl = calculateLvl(exp);
        Integer untilNextLvl = calculateUntilNextLevel(lvl,exp);

        player.setLevel(lvl);
        player.setUntilNextLevel(untilNextLvl);
    }

    public int calculateLvl(Integer exp){
        return (int)(Math.sqrt(2500+200*exp) - 50)/100;
    }

    public int calculateUntilNextLevel(Integer lvl, Integer exp){
        return 50*(lvl+1)*(lvl+2) - exp;
    }
}
