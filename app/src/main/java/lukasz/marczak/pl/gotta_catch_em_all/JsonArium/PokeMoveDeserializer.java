package lukasz.marczak.pl.gotta_catch_em_all.JsonArium;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.Realm;
import lukasz.marczak.pl.gotta_catch_em_all.data.PokeMove;
import lukasz.marczak.pl.gotta_catch_em_all.data.realm.RealmMove;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeMoveDeserializer implements JsonDeserializer<PokeMove> {
    public static Context context;
    public static PokeMoveDeserializer instance = new PokeMoveDeserializer();
    public static final String TAG = PokeMoveDeserializer.class.getSimpleName();

    public static PokeMoveDeserializer getInstance(Context c) {
        context = c;
        return instance;
    }

    @Override
    public PokeMove deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext con) throws JsonParseException {

        Log.d(TAG, "received json: " + json.toString());
        JsonObject obj = json.getAsJsonObject();

        int accuracy = obj.get("accuracy").getAsInt();
        int id = obj.get("id").getAsInt();
        int pp = obj.get("pp").getAsInt();
        int power = obj.get("power").getAsInt();

        String name = obj.get("name").getAsString();
        String created = obj.get("category").getAsString();
        String category = obj.get("category").getAsString();
        String modified = obj.get("modified").getAsString();
        String description = obj.get("description").getAsString();
        String resourceUri = obj.get("resource_uri").getAsString();

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        RealmMove move = realm.createObject(RealmMove.class);
        move.setId(id);
        move.setPp(pp);
        move.setName(name);
        move.setPower(power);
        move.setCreated(created);
        move.setAccuracy(accuracy);
        move.setCategory(category);
        move.setModified(modified);
        move.setDescription(description);
        move.setResourceUri(resourceUri);
        realm.commitTransaction();
        realm.close();
        return new PokeMove(id, pp, power, accuracy, name,
                created, category, modified, description, resourceUri);
    }
}
