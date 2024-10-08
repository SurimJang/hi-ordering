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
    MenuRepository menuRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderPlacedEvent'"
    )
    public void wheneverOrderPlacedEvent_DecreaseMenuPolicy(
        @Payload OrderPlacedEvent orderPlacedEvent
    ) {
        OrderPlacedEvent event = orderPlacedEvent;
        System.out.println(
            "\n\n##### listener DecreaseMenuPolicy : " +
            orderPlacedEvent +
            "\n\n"
        );

        // Sample Logic //
        Menu.decreaseMenuPolicy(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderCancelledEvent'"
    )
    public void wheneverOrderCancelledEvent_IncreaseMenuPolicy(
        @Payload OrderCancelledEvent orderCancelledEvent
    ) {
        OrderCancelledEvent event = orderCancelledEvent;
        System.out.println(
            "\n\n##### listener IncreaseMenuPolicy : " +
            orderCancelledEvent +
            "\n\n"
        );

        // Sample Logic //
        Menu.increaseMenuPolicy(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
