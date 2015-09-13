package lukasz.marczak.pl.gotta_catch_em_all.JsonArium;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.data.PokeDetail;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class PokeDetailDeserializer implements JsonDeserializer<PokeDetail> {

    public static final String TAG = PokeDetailDeserializer.class.getSimpleName();
    public static JsonDeserializer INSTANCE = new PokeDetailDeserializer();

    @Override
    public PokeDetail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Log.d(TAG, "received json: " + json.toString());
        PokeDetail pokeDetail = new PokeDetail();

        JsonObject object = json.getAsJsonObject();

        String attack = object.get("attack").getAsString();
        pokeDetail.setAttack(attack);

        String defense = object.get("defense").getAsString();
        pokeDetail.setDefense(defense);

        String sp_atk = object.get("sp_atk").getAsString();
        pokeDetail.setSp_atk(sp_atk);

        String sp_def = object.get("sp_def").getAsString();
        pokeDetail.setSp_def(sp_def);

        String height = object.get("height").getAsString();
        pokeDetail.setHeight(height);

        String weight = object.get("weight").getAsString();
        pokeDetail.setWeight(weight);

        String hp = object.get("hp").getAsString();
        pokeDetail.setHp(hp);

        String speed = object.get("speed").getAsString();
        pokeDetail.setSpeed(speed);

        String happy = object.get("happiness").getAsString();
        pokeDetail.setHappiness(happy);

        String name = object.get("name").getAsString();
        pokeDetail.setName(name);

        String id = object.get("pkdx_id").getAsString();
        pokeDetail.setId(id);


        JsonArray evolutions = object.get("evolutions").getAsJsonArray();

        List<String> pokemon_evolutions = new ArrayList<>();
        if (evolutions != null) {
            for (int j = 0; j < evolutions.size(); j++) {
                pokemon_evolutions.add(
                        evolutions.get(j).getAsJsonObject().get("to").getAsString()
                );
            }
        }
        pokeDetail.setEvolvesIntoList(pokemon_evolutions);
        JsonArray types = object.get("types").getAsJsonArray();

        List<String> pokemon_types = new ArrayList<>();
        if (types != null) {
            for (int j = 0; j < types.size(); j++) {
                pokemon_types.add(
                        types.get(j).getAsJsonObject().get("name").getAsString()
                );
            }
        }
        pokeDetail.setTypes(pokemon_types);
        return pokeDetail;
    }
}
