package edu.java.bot.utils.retry;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.retry.ExhaustedRetryException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class RetryLinear extends Retry {

    private final Duration maxBackoff = Duration.ofMillis(Long.MAX_VALUE);

    private final long maxAttempts;

    private final Duration minBackoff;

    private final Predicate<Throwable> errorFilter;

    private final Supplier<Scheduler> schedulerSupplier = Schedulers::parallel;

    public static RetryLinear linear(long maxAttempts, Duration minBackoff) {
        return new RetryLinear(maxAttempts, minBackoff, t -> true);
    }

    public RetryLinear filter(Predicate<Throwable> errorFilter) {
        return new RetryLinear(
            this.maxAttempts,
            this.minBackoff,
            errorFilter
        );
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> t) {
        return Flux.deferContextual(cv ->
            t.contextWrite(cv)
                .concatMap(retryWhenState -> {
                    RetrySignal copy = retryWhenState.copy();
                    Throwable currentFailure = copy.failure();
                    long iteration = copy.totalRetries();
                    if (currentFailure == null) {
                        return Mono.error(
                            new IllegalStateException("Retry.RetrySignal#failure() not expected to be null")
                        );
                    }
                    if (!errorFilter.test(currentFailure)) {
                        return Mono.error(currentFailure);
                    }
                    if (iteration >= maxAttempts) {
                        return Mono.error(new ExhaustedRetryException("Retry exhausted: " + this));
                    }
                    Duration nextBackoff;
                    try {
                        nextBackoff = minBackoff.multipliedBy(iteration + 1);
                        if (nextBackoff.compareTo(maxBackoff) > 0) {
                            nextBackoff = maxBackoff;
                        }
                    } catch (ArithmeticException overflow) {
                        nextBackoff = maxBackoff;
                    }
                    return Mono.delay(nextBackoff, schedulerSupplier.get()).contextWrite(cv);
                })
                .onErrorStop()
        );
    }
}
