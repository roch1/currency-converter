package currencyconverter.http;

import java.net.http.HttpClient;
import java.util.concurrent.Executor;

public class CurrencyConverterHttpClient {

    private final Executor threadPool;
    private HttpClient httpClient;

    public CurrencyConverterHttpClient(Executor threadPool) {
        this.threadPool = threadPool;
    }

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = createHttpClient();
        }
        return httpClient;
    }

    private HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .executor(threadPool)
                .build();
    }

}
