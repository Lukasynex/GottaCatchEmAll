package lukasz.marczak.pl.gotta_catch_em_all.data;

import io.realm.RealmObject;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class RealmPoke extends RealmObject {
    private String id;
    private String name;

    public RealmPoke() {
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
}
