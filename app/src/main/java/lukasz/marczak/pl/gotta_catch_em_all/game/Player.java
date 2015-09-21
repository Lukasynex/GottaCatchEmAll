package lukasz.marczak.pl.gotta_catch_em_all.game;

import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;

/**
 * Created by Lukasz Marczak on 2015-09-20.
 */
public class Player {
    private PokeDetail detail;
    private int currentLevel;
    private String pokeName;
    private int hp;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Player(String p) {
        pokeName = p;
    }

    public Player() {
        hp = 100;
    }

    public PokeDetail getDetail() {
        return detail;
    }

    public void setDetail(PokeDetail detail) {
        this.detail = detail;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getName() {
        return detail.getName();
    }
}
