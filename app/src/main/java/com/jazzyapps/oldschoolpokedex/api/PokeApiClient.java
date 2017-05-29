package com.jazzyapps.oldschoolpokedex.api;

import com.jazzyapps.oldschoolpokedex.model.Pokemon.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeApiClient {

  @GET("pokemon/{pokemon_id}")
  Call<Pokemon> getPokemon(@Path("pokemon_id") String pokemonId);

}
