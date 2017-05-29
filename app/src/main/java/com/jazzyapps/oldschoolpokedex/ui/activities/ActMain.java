package com.jazzyapps.oldschoolpokedex.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jazzyapps.oldschoolpokedex.R;
import com.jazzyapps.oldschoolpokedex.api.PokeApiClient;
import com.jazzyapps.oldschoolpokedex.base.Constants;
import com.jazzyapps.oldschoolpokedex.model.Pokemon.Pokemon;

import org.w3c.dom.Text;

import java.io.IOError;
import java.io.IOException;

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

  PokeApiClient pokeApiClient;

  @BindView(R.id.tv_name)
  TextView tvName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.tv_name)
  public void onClick(View view) {
    tvName.setText("derp");
    if (pokeApiClient == null) {
      initPokeApiClient();
    }

    int pokemonId = 277;

    Call<Pokemon> call = pokeApiClient.getPokemon(pokemonId);
    call.enqueue(new Callback<Pokemon>() {
      @Override
      public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
        Pokemon pokemon = response.body();
        int statusCode = response.code();
        Log.d(TAG, "pokemon call worked! status code:" + statusCode + "pokemon name:" + pokemon.getName());
        tvName.setText(pokemon.getName());
      }

      @Override
      public void onFailure(Call<Pokemon> call, Throwable t) {
        Log.d(TAG, "-----Failure when calling pokemon get-----");
      }
    });
  }

  private void initPokeApiClient() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.POKEAPI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    pokeApiClient = retrofit.create(PokeApiClient.class);
  }


}
