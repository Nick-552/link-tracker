package edu.java.scrapper.util.retry;

import edu.java.scrapper.configuration.RetryConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@UtilityClass
public class RetryFactory {

    Map<RetryType, Function<RetryConfig.WebClientRetryConfig, Retry>> map = new HashMap<>();

    static {
        map.put(RetryType.FIXED, webClientRetryConfig -> Retry
            .fixedDelay(
                webClientRetryConfig.maxRetries(),
                webClientRetryConfig.initialInterval()
            ).filter(createErrorFilter(webClientRetryConfig.codes()))
        );
        map.put(RetryType.LINEAR, webClientRetryConfig -> RetryLinear
            .linear(
                webClientRetryConfig.maxRetries(),
                webClientRetryConfig.initialInterval()
            ).filter(createErrorFilter(webClientRetryConfig.codes()))
        );
        map.put(RetryType.EXPONENTIAL, webClientRetryConfig -> Retry
            .backoff(
                webClientRetryConfig.maxRetries(),
                webClientRetryConfig.initialInterval()
            ).filter(createErrorFilter(webClientRetryConfig.codes()))
        );
    }

    public static ExchangeFilterFunction createFilter(Retry retry) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            }).retryWhen(retry);
    }

    public static Retry create(RetryConfig.WebClientRetryConfig webClientRetryConfig) {
        return map.get(webClientRetryConfig.retryType())
            .apply(webClientRetryConfig);
    }

    private static Predicate<Throwable> createErrorFilter(List<Integer> codes) {
        return throwable -> {
            if (throwable instanceof WebClientResponseException webClientResponseException) {
                return codes.contains(webClientResponseException.getStatusCode().value());
            }
            return true;
        };
    }
}
