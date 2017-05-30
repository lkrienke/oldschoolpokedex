package com.jazzyapps.oldschoolpokedex.ui.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jazzyapps.oldschoolpokedex.R;
import com.jazzyapps.oldschoolpokedex.api.PokeApiClient;
import com.jazzyapps.oldschoolpokedex.base.Constants;
import com.jazzyapps.oldschoolpokedex.model.Pokemon.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActMain extends AppCompatActivity {
  private final String TAG = getClass().getSimpleName();

  // saveInstanceState Keys
  private final String ET_FILLED_TEXT = "et_filled_text";
  private final String TV_NAME = "tv_name_filled";
  private final String TV_NUMBER = "tv_number_filled";
  private final String TV_HEIGHT = "tv_height_filled";
  private final String TV_WEIGHT = "tv_weight_filled";

  private final String DEFAULT_IMG_KEY = "front_default";

  Context application;
  PokeApiClient pokeApiClient;

  @BindView(R.id.et_poke_search)
  EditText etPokeSearch;
  @BindView(R.id.tv_submit)
  TextView tvSubmit;

  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.tv_number)
  TextView tvNumber;
  @BindView(R.id.tv_height)
  TextView tvHeight;
  @BindView(R.id.tv_weight)
  TextView tvWeight;
  @BindView(R.id.iv_main)
  ImageView ivMain;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_main);
    ButterKnife.bind(this);
    this.application = getApplicationContext();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putString(ET_FILLED_TEXT, etPokeSearch.getText().toString());
    outState.putString(TV_NAME , tvName.getText().toString());
    outState.putString(TV_NUMBER , tvNumber.getText().toString());
    outState.putString(TV_HEIGHT , tvHeight.getText().toString());
    outState.putString(TV_WEIGHT , tvWeight.getText().toString());

    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    etPokeSearch.setText(savedInstanceState.getString(ET_FILLED_TEXT));
    tvName.setText(savedInstanceState.getString(TV_NAME));
    tvNumber.setText(savedInstanceState.getString(TV_NUMBER));
    tvHeight.setText(savedInstanceState.getString(TV_HEIGHT));
    tvWeight.setText(savedInstanceState.getString(TV_WEIGHT));
  }

  @OnClick(R.id.tv_submit)
  public void onClick(View view) {
    if (pokeApiClient == null) {
      initPokeApiClient();
    }

    tvSubmit.setClickable(false);

    String pokemonId = etPokeSearch.getText().toString();

    if (!pokemonId.isEmpty()) {

      Call<Pokemon> call = pokeApiClient.getPokemon(pokemonId);
      call.enqueue(new Callback<Pokemon>() {
        @Override
        public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
          Pokemon pokemon = response.body();
          int statusCode = response.code();

          if (pokemon == null) {
            Toast.makeText(application, "Invalid pokemon :(", Toast.LENGTH_LONG).show();
          } else {
            Log.d(TAG, "pokemon call worked! status code:" + statusCode + "pokemon name:" + pokemon.getName());

            String name = pokemon.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            String id = String.valueOf(pokemon.getId());
            String height = String.valueOf(pokemon.getHeight());
            String weight = String.valueOf(pokemon.getWeight());
            Map<String, String> sprites = pokemon.getSprites();
            String url = sprites.get(DEFAULT_IMG_KEY);

            tvName.setText("Name: " + name);
            tvNumber.setText("Number: " + id);
            tvHeight.setText("Height: " + height);
            tvWeight.setText("Weight: " + weight);
            Picasso.with(application).load(url).into(ivMain);
          }

          tvSubmit.setClickable(true);
        }

        @Override
        public void onFailure(Call<Pokemon> call, Throwable t) {
          Toast.makeText(application, "API failure occured", Toast.LENGTH_LONG).show();
          Log.d(TAG, "-----Failure when calling pokemon get-----");

          tvSubmit.setClickable(true);
        }
      });
    }
  }

  private void initPokeApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.POKEAPI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    pokeApiClient = retrofit.create(PokeApiClient.class);
  }
}
