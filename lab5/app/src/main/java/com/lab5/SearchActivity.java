package com.lab5;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.PublicKey;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<Feed> medias = new ArrayList<Feed>();
    private ArrayList<Breed> breeds = new ArrayList<Breed>();
    private ArrayDeque<String> starred = new ArrayDeque<String>(10);
    private ListView listView;
    private Spinner breed;
    private int chosenBreed = 0;
    private String id = "";
    private int spinnerId = 0;
    private ArrayList<String> breedSpinner = new ArrayList<>();
    Adapter startAdapter;
    StarredAdapter starredAdapter;
    Adapter updateAdapter;

    public static final String BREED_URL = "http://api.example.com/";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        ArrayList<Cat> cats = new ArrayList<>();
        GetCats getCats = new GetCats();
        getCats.execute();

        try {
            cats = getCats.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (cats != null) {
            for (Cat cat : cats
            ) {
                breeds.add(new Breed(spinnerId, cat.getId(), cat.getName()));
                breedSpinner.add(cat.getName());
                spinnerId++;
            }

            Load load = (Load) new Load();
            load.execute(id);
            try {
                medias = load.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ;
        }

        listView = findViewById(R.id.listView);
        listView.addFooterView(footer);

        startAdapter = new Adapter(medias, getApplicationContext());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    Load load = (Load) new Load().execute(id);
                    try {
                        medias.addAll(load.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startAdapter.notifyDataSetChanged();
                    //}
                }
                startAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(startAdapter);
    }

    public void onClickSearch(View view) {
        setContentView(R.layout.activity_search);
        breed = (Spinner) findViewById(R.id.breed);
        if (breedSpinner.size() > 0) {
            ArrayAdapter<String> adapterBreed = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, breedSpinner);
            adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            breed.setAdapter(adapterBreed);
        }

        if (chosenBreed >= 0)
            breed.setSelection(chosenBreed);
    }

    public void onClickStarred(View view) {
        setContentView(R.layout.starred);
        listView = findViewById(R.id.listViewStarred);

        starred = Adapter.getStarred();
        starredAdapter = new StarredAdapter(new ArrayList(starred), getApplicationContext());
        listView.setAdapter(starredAdapter);
    }

    public void onClickConfirm(View view) {

        setContentView(R.layout.view);
        listView = findViewById(R.id.listView);
        chosenBreed = Integer.parseInt(String.valueOf(breed.getSelectedItemId()));

        String id1 = null;
        while (id1 == null) {
            for (Breed breed : breeds
            ) {
                if (chosenBreed == breed.getSpinnerId()) {
                    id1 = breed.getId();
                    break;
                }
            }
        }
        id = id1;

        updateMain();
    }

    public void onClickBack(View view) {
        setContentView(R.layout.view);
        listView = findViewById(R.id.listView);
        updateMain();
    }

    public void updateMain() {
        Load load = (Load) new Load().execute(id);

        try {
            medias = load.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateAdapter = new Adapter(medias, getApplicationContext());
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    Load load = (Load) new Load().execute(id);
                    try {
                        medias.addAll(load.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateAdapter.notifyDataSetChanged();
                    //}
                }
                updateAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(updateAdapter);
    }

    class Breed {
        private int spinnerId;
        private String id;
        private String breed;

        Breed(int spinnerId, String id, String breed) {
            this.spinnerId = spinnerId;
            this.id = id;
            this.breed = breed;
        }

        public int getSpinnerId() {
            return spinnerId;
        }

        public String getBreed() {
            return breed;
        }

        public String getId() {
            return id;
        }
    }

    private class Load extends AsyncTask<String, Void, ArrayList<Feed>> {

        @Override
        protected ArrayList<Feed> doInBackground(String... path) {
            String url = "https://api.thecatapi.com/v1/images/search?breed_ids=" + path[0];

            ArrayList<Feed> feeds = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Person cat = null;
                try {
                    cat = new ParseJSON(url).getCat();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                feeds.add(new Feed(cat.getUrl(), 0));
            }
            return feeds;
        }
    }

    private class GetCats extends AsyncTask<String, Void, ArrayList<Cat>> {
        @Override
        protected ArrayList<Cat> doInBackground(String... path) {
            ParseJSON parseJSON = new ParseJSON("https://api.thecatapi.com/v1/breeds?attach_breed=1)");
            try {
                return parseJSON.getCats();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
