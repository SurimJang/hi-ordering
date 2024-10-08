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
    ShopRepository shopRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
    }

    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='OrderConfirmedEvent'")
    public void wheneverOrderConfirmedEvent_StartCookPolicy(
            @Payload OrderConfirmedEvent orderConfirmedEvent) {
        OrderConfirmedEvent event = orderConfirmedEvent;
        System.out.println(
                "\n\n##### listener StartCookPolicy : " +
                        orderConfirmedEvent +
                        "\n\n");

        // Sample Logic //
        Shop.startCookPolicy(event);
    }

    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='OrderCancelledEvent'")
    public void wheneverOrderCancelledEvent_CancelCookPolicy(
            @Payload OrderCancelledEvent orderCancelledEvent) {
        OrderCancelledEvent event = orderCancelledEvent;
        System.out.println(
                "\n\n##### listener CancelCookPolicy : " +
                        orderCancelledEvent +
                        "\n\n");

        // Sample Logic //
        Shop.cancelCookPolicy(event);
    }
}
// >>> Clean Arch / Inbound Adaptor
