package guru.qa.niffler.api.okhttp;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertResponseHttpCodeIsSuccessfulInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        assertTrue(response.isSuccessful());
        return response;
    }
}
