package ua.dp.strahovik.yalantistask1.services.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.entities.deserializers.EventDeserializer;


public interface EventService {

    String ENDPOINT = "http://dev-contact.yalantis.com/";

    @GET("rest/v1/tickets")
    Observable<List<Event>> getEvents(@Query("state") String state, @Query("offset") String offset,
                                      @Query("amount") String amount);

    class Factory {

        public static EventService newEventService(Context context) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Event.class, new EventDeserializer(context))
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EventService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(EventService.class);
        }
    }
}
