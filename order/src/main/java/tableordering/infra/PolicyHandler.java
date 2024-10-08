package tableordering.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tableordering.config.kafka.KafkaProcessor;
import tableordering.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OutOfStockEvent'"
    )
    public void wheneverOutOfStockEvent_UpdateStatusPolicy(
        @Payload OutOfStockEvent outOfStockEvent
    ) {
        OutOfStockEvent event = outOfStockEvent;
        System.out.println(
            "\n\n##### listener UpdateStatusPolicy : " +
            outOfStockEvent +
            "\n\n"
        );

        // Sample Logic //
        Order.updateStatusPolicy(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentCompleteEvent'"
    )
    public void wheneverPaymentCompleteEvent_UpdateStatusPolicy(
        @Payload PaymentCompleteEvent paymentCompleteEvent
    ) {
        PaymentCompleteEvent event = paymentCompleteEvent;
        System.out.println(
            "\n\n##### listener UpdateStatusPolicy : " +
            paymentCompleteEvent +
            "\n\n"
        );

        // Sample Logic //
        Order.updateStatusPolicy(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
