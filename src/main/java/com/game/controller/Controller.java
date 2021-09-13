package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/")
public class Controller {

    @Autowired
    private PlayerService playerService;

    @GetMapping("players")
    public List<Player> getAllPlayers(
            @RequestParam (value = "name", required = false) String name,
            @RequestParam (value = "title", required = false) String title,
            @RequestParam (value = "race", required = false) Race race,
            @RequestParam (value = "profession", required = false) Profession profession,
            @RequestParam (value = "after", required = false) Long after,
            @RequestParam (value = "before", required = false) Long before,
            @RequestParam (value = "banned", required = false) Boolean banned,
            @RequestParam (value = "minExperience", required = false) Integer minExperience,
            @RequestParam (value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam (value = "minLevel", required = false) Integer minLevel,
            @RequestParam (value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam (value = "order", required = false) PlayerOrder order,
            @RequestParam (value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam (value = "pageSize", required = false) Integer pageSize) {

        List<Player> players = playerService.getAllPlayers(name, title, race, profession, after,
                before, banned, minExperience, maxExperience, minLevel, maxLevel);

        players = playerService.sortPlayers(players, order);

        return playerService.getPage(players,pageNumber,pageSize);
    }

    @GetMapping("players/count")
    public Integer getCount(  @RequestParam (value = "name", required = false) String name,
                              @RequestParam (value = "title", required = false) String title,
                              @RequestParam (value = "race", required = false) Race race,
                              @RequestParam (value = "profession", required = false) Profession profession,
                              @RequestParam (value = "after", required = false) Long after,
                              @RequestParam (value = "before", required = false) Long before,
                              @RequestParam (value = "banned", required = false) Boolean banned,
                              @RequestParam (value = "minExperience", required = false) Integer minExperience,
                              @RequestParam (value = "maxExperience", required = false) Integer maxExperience,
                              @RequestParam (value = "minLevel", required = false) Integer minLevel,
                              @RequestParam (value = "maxLevel", required = false) Integer maxLevel){

        List<Player> players = playerService.getAllPlayers(name, title, race, profession, after,
                before, banned, minExperience, maxExperience, minLevel, maxLevel);

        return players.size();
    }

    @GetMapping("players/{id}")
    public Player getPlayer(@PathVariable String id){
        return playerService.getPlayer(id);
    }

    @DeleteMapping("players/{id}")
    public void deletePlayer(@PathVariable String id){
        playerService.deletePlayer(id);
    }

    @PostMapping("players")
    public Player addNewPlayer(@RequestBody Player player){
        return playerService.addNewPlayer(player);
    }

    @PostMapping("players/{id}")
    public Player updatePlayer(@PathVariable String id, @RequestBody Player player){
        return playerService.updatePlayer(id,player);
    }
}
