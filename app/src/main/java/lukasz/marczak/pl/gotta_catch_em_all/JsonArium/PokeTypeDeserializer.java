package lukasz.marczak.pl.gotta_catch_em_all.JsonArium;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import lukasz.marczak.pl.gotta_catch_em_all.data.PokeType;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeTypeDeserializer implements JsonDeserializer<PokeType> {

    public static final String TAG = PokeTypeDeserializer.class.getSimpleName();
    public static PokeTypeDeserializer INSTANCE = new PokeTypeDeserializer();

    @Override
    public PokeType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Log.d(TAG, "received json: " + json.toString());
        JsonObject obj = json.getAsJsonObject();
        int id = obj.get("id").getAsInt();
        String name = obj.get("name").getAsString();
        JsonArray in_effective = obj.get("ineffective").getAsJsonArray();
        JsonArray super_effective = obj.get("super_effective").getAsJsonArray();
        JsonArray weak_ness = obj.get("weakness").getAsJsonArray();

        String inEffective ="";
        String superEffective=""  ;
        String weak ="" ;

        if (in_effective != null && in_effective.size() > 0)
            for (int x = 0; x < in_effective.size(); x++)
                inEffective+=in_effective.get(x).getAsJsonObject().get("name").getAsString()+"|";

        if (super_effective != null && super_effective.size() > 0)
            for (int x = 0; x < super_effective.size(); x++)
                superEffective+=super_effective.get(x).getAsJsonObject().get("name").getAsString()+"|";

        if (weak_ness != null && weak_ness.size() > 0)
            for (int x = 0; x < weak_ness.size(); x++)
                weak+=weak_ness.get(x).getAsJsonObject().get("name").getAsString()+"|";

        PokeType pokeType = new PokeType();
        pokeType.setName(name);
        pokeType.setId(id);
        pokeType.setIneffective(inEffective);
        pokeType.setSuperEffective(superEffective);
        pokeType.setWeakness(weak);

        return pokeType;
    }
}