package lukasz.marczak.pl.gotta_catch_em_all.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tt.whorlviewlibrary.WhorlView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lukasz.marczak.pl.gotta_catch_em_all.JsonArium.PokeNetNameDeserializer;
import lukasz.marczak.pl.gotta_catch_em_all.R;
import lukasz.marczak.pl.gotta_catch_em_all.adapters.PokedexAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.configuration.Config;
import lukasz.marczak.pl.gotta_catch_em_all.connection.PokedexService;
import lukasz.marczak.pl.gotta_catch_em_all.connection.SimpleRestAdapter;
import lukasz.marczak.pl.gotta_catch_em_all.data.NetPoke;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;

public class PokedexFragment extends Fragment {

    public static final String TAG = PokedexFragment.class.getSimpleName();

    PokedexAdapter pokedexAdapter;
    List<NetPoke> dataset;
    RecyclerView recyclerView;
    WhorlView progressBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public static PokedexFragment newInstance() {
        PokedexFragment fragment = new PokedexFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public PokedexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (WhorlView) view.findViewById(R.id.whorlView);
        showProgressBar();

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        dataset = new ArrayList<>();
        for (int j = 0; j < 150; j++) {
            dataset.add(new NetPoke(j + 1, ""));
        }
        pokedexAdapter = new PokedexAdapter(this, dataset, recyclerView) {
            @Override
            public void loadNewNetPokes(int position, PokedexAdapter pokedexAdapter) {
                showProgressBar();
                downloadMorePokes(position, pokedexAdapter);
            }
        };
        recyclerView.setAdapter(pokedexAdapter);
        setupRxQuerying(pokedexAdapter);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }

    private boolean isRunningGETs = false;

    private void downloadMorePokes(int position, PokedexAdapter adapter) {
        Log.d(TAG, "downloadMorePokes()");
        if (isRunningGETs)
            return;
        PokedexService service = new SimpleRestAdapter(PokedexService.POKEDEX_NAME_ENDPOINT,
                new TypeToken<String>() {
                }.getType(), new PokeNetNameDeserializer()).getPokedexService();

        int maxSize = (position + Config.MORE_POKES) > Config.MAX_POKES
                ? Config.MAX_POKES : position + Config.MORE_POKES;
        List<rx.Observable<Response>> observables = new ArrayList<>();


        Log.d(TAG, "downloading items: (" + position + ", " + maxSize + ")");

        for (int j = position ; j < maxSize; j++) {
            rx.Observable<Response> newPokemon = service.getPokemonNameByID(j + 1)
                    .onErrorResumeNext(Observable.<Response>empty());
            observables.add(newPokemon);
        }
        Observable.merge(observables).subscribe(doHttpGETs(adapter));
    }

    private void setupRxQuerying(PokedexAdapter adapter) {
        Log.d(TAG, "setupRxQuerying()");
        List<rx.Observable<Response>> observables = new ArrayList<>();

        PokedexService service = new SimpleRestAdapter(
                PokedexService.POKEDEX_NAME_ENDPOINT, Response.class,
                new PokeNetNameDeserializer()).getPokedexService();

        for (int j = 0; j < 20; j++) {
            rx.Observable<Response> newPokemon = service.getPokemonNameByID(j + 1)
                    .onErrorResumeNext(Observable.<Response>empty());
            observables.add(newPokemon);
        }
        Observable.merge(observables).subscribe(doHttpGETs(adapter));
    }

    private Subscriber<Response> doHttpGETs(final PokedexAdapter adapter) {
        return new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataset();
                        dismissProgressBar();
                        adapter.unLockTouchEvents();
                        isRunningGETs = false;
                        PokedexAdapter.downloadDone = true;
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError()");
                Log.e(TAG, "message : " + e.getMessage());
                Log.e(TAG, "cause : " + e.getCause());
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataset();
                        dismissProgressBar();
                        adapter.unLockTouchEvents();
                        isRunningGETs = false;
                    }
                });
            }

            @Override
            public void onNext(Response s) {
                Log.d(TAG, "onNext()");
                consumeResponse(s);
            }
        };
    }

    public void consumeResponse(Response response) {
        if (response.getBody() == null) {
            Log.e(TAG, "null body at " + response.getUrl());
            return;
        }

        Type collectionType = new TypeToken<String>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(collectionType, new PokeNetNameDeserializer());
        Gson gson = gsonBuilder.create();
        GsonConverter converter = new GsonConverter(gson);

        String result = null;
        try {
            result = (String) converter.fromBody(response.getBody(), collectionType);
        } catch (ConversionException e) {
            e.printStackTrace();
            Log.d(TAG, "still null ;(");
        } finally {
            if (result != null) {

                String[] slices = response.getUrl().split("sprite");
                String str = slices[1].replaceAll("/", "");

                int position = Integer.valueOf(str);
                Log.d(TAG, "setting name: " + result + ", for position: " + (position - 1));
                pokedexAdapter.getDataset().get(position - 1).setName(result);
            }
        }
    }

    public void dismissProgressBar() {
        progressBar.setVisibility(View.GONE);
        progressBar.stop();
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.start();
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
