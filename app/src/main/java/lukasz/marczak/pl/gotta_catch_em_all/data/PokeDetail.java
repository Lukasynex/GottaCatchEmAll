package lukasz.marczak.pl.gotta_catch_em_all.data;

import java.util.List;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class PokeDetail {
    /**
     * basic info
     */
    private String id;
    private String name;
    /**
     * fighting details
     */
    private String attack;
    private String defense;
    private String sp_def;
    private String sp_atk;
    /**
     * physical details
     */
    private String height;
    private String weight;
    private String hp;
    private String speed;
    private String happiness;
    private List<String> evolvesIntoList;
    private List<String> types;

    public List<String> getEvolvesIntoList() {
        return evolvesIntoList;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setEvolvesIntoList(List<String> evolvesIntoList) {
        this.evolvesIntoList = evolvesIntoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public String getSp_def() {
        return sp_def;
    }

    public void setSp_def(String sp_def) {
        this.sp_def = sp_def;
    }

    public String getSp_atk() {
        return sp_atk;
    }

    public void setSp_atk(String sp_atk) {
        this.sp_atk = sp_atk;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHappiness() {
        return happiness;
    }

    public void setHappiness(String happiness) {
        this.happiness = happiness;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public PokeDetail() {
    }

    @Override
    public String toString() {
        String softTypes = "", evolves = "";
        String evolvesInto = "", _types="";


        if (!types.isEmpty()) {
            for (int k = 0; k < types.size(); k++) {
                String s = types.get(k);
                if (k != types.size() - 1)
                    softTypes += s + ",";
                else softTypes += s;

            }
            _types = "types : " + softTypes;

        }



        if (!evolvesIntoList.isEmpty()) {
            for (int k = 0; k < evolvesIntoList.size(); k++) {
                String s = evolvesIntoList.get(k);
                if (k != evolvesIntoList.size() - 1)
                    evolves += s + ",";
                else evolves += s;

            }
            evolvesInto = "Evolves into " + evolves;

        }

        return "attack : " + attack + "\n"
                + "defense : " + defense + "\n"
                + "HP : " + hp + "\n"
                + "height : " + height + "\n"
                + "weight : " + weight + "\n"
                + "speed : " + speed + "\n"
                + _types + "\n"
                + evolvesInto;
    }
}
