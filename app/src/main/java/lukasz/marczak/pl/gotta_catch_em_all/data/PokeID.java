package lukasz.marczak.pl.gotta_catch_em_all.data;


/**
 * Created by Lukasz Marczak on 2015-09-19.
 */
public class PokeID {

    private int id;
    private String name;

    public PokeID(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
