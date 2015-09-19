package lukasz.marczak.pl.gotta_catch_em_all.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class RealmID extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;

    public RealmID() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }
}
