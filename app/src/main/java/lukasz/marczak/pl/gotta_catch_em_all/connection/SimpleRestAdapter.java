package lukasz.marczak.pl.gotta_catch_em_all.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class SimpleRestAdapter {


    public RestAdapter getAdapter() {
        return adapter;
    }

    private RestAdapter adapter;

    public PokedexService getPokedexService(){
        return adapter.create(PokedexService.class);
    }
    /**
     * bieda version
     */
    public SimpleRestAdapter(String endpoint) {

        adapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .build();
    }

    /**
     * full version
     */
    public SimpleRestAdapter(String endpoint, Type collectionType,
                             JsonDeserializer deserializer) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(collectionType, deserializer);
        Gson gson = builder.create();
        GsonConverter converter = new GsonConverter(gson);

        adapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(converter)
                .build();
    }
}
