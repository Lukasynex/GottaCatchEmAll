package lukasz.marczak.pl.gotta_catch_em_all.JsonArium;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import lukasz.marczak.pl.gotta_catch_em_all.data.PokeAbility;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeAbilityDeserializer implements JsonDeserializer<PokeAbility> {

    public static final String TAG = PokeAbilityDeserializer.class.getSimpleName();
    public static PokeAbilityDeserializer INSTANCE = new PokeAbilityDeserializer();

    @Override
    public PokeAbility deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Log.d(TAG, "received json: " + json.toString());
        JsonObject obj = json.getAsJsonObject();

        int id = obj.get("id").getAsInt();

        String name = obj.get("name").getAsString();
        String created = obj.get("category").getAsString();
        String modified = obj.get("modified").getAsString();
        String description = obj.get("description").getAsString();
        String resourceUri = obj.get("resource_uri").getAsString();

        return new PokeAbility(id, name, created, modified, description, resourceUri);
    }
}
