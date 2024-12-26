package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.okhttp.AssertResponseHttpCodeIsSuccessfulInterceptor;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class SpendApiClient {

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new AssertResponseHttpCodeIsSuccessfulInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(okHttpClient)
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @SneakyThrows
    public SpendJson createSpend(SpendJson spend) {
        return spendApi.addSpend(spend).execute().body();
    }

    @SneakyThrows
    public SpendJson getSpend(String id) {
        return spendApi.getSpend(id).execute().body();
    }

    @SneakyThrows
    public List<SpendJson> getAllSpends() {
        return spendApi.getAllSpends().execute().body();
    }

    @SneakyThrows
    public SpendJson editSpend(SpendJson spend) {
        return spendApi.editSpend(spend).execute().body();
    }

    @SneakyThrows
    public void deleteSpend(String username, List<String> ids) {
        spendApi.removeSpend(username, ids).execute();
    }

    @SneakyThrows
    public CategoryJson addCategory(CategoryJson categoryJson) {
        return spendApi.addCategory(categoryJson).execute().body();
    }

    @SneakyThrows
    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return spendApi.updateCategory(categoryJson).execute().body();
    }

    @SneakyThrows
    public List<CategoryJson> getAllCategories() {
        return spendApi.gatAllCategories().execute().body();
    }

}
