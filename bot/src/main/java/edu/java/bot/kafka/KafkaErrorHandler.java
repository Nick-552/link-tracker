package edu.java.bot.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Log4j2
@RequiredArgsConstructor
public class KafkaErrorHandler implements CommonErrorHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String dlqSuffix;


    @Override
    public void handleOtherException(
        @NotNull Exception thrownException,
        @NotNull Consumer<?, ?> consumer,
        @NotNull MessageListenerContainer container,
        boolean batchListener
    ) {
        if (thrownException instanceof RecordDeserializationException ex) {
            log.error("Deserialization exception", thrownException);
            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        } else {
            log.error("Exception not handled", thrownException);
        }
    }

    @Override
    public boolean handleOne(
        @NotNull Exception thrownException,
        @NotNull ConsumerRecord<?, ?> consumerRecord,
        @NotNull Consumer<?, ?> consumer,
        @NotNull MessageListenerContainer container
    ) {
        log.error("Exception thrown", thrownException);
        consumer.seek(
            new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
            consumerRecord.offset() + 1L
        );
        consumer.commitSync();
        kafkaTemplate.send(consumerRecord.topic() + dlqSuffix, consumerRecord.value());
        log.info("Message sent to DLQ");
        return true;
    }
}
