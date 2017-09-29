package id.co.imastudio1.sinaumaps.restApi;


import id.co.imastudio1.sinaumaps.model.PlaceModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {

    @GET("api/directions/json")
    Call<PlaceModel> request_route(
                @Query("origin") String origin,
                @Query("destination") String tujuan

    );

}
