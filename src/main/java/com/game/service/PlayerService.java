package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PlayerService {
    Player getPlayer(String id);

    List<Player> getAllPlayers(String name,
                               String title,
                               Race race,
                               Profession profession,
                               Long after,
                               Long before,
                               Boolean banned,
                               Integer minExperience,
                               Integer maxExperience,
                               Integer minLevel,
                               Integer maxLevel);

    List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize);

    List<Player> sortPlayers(List<Player> players, PlayerOrder order);

    void deletePlayer(String id);

    Player addNewPlayer(Player player);

    Player updatePlayer(String id, Player player);
}


