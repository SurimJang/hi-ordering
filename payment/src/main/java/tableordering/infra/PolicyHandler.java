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
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='MenuDecreasedEvent'"
    )
    public void wheneverMenuDecreasedEvent_PaymentRequestPolicy(
        @Payload MenuDecreasedEvent menuDecreasedEvent
    ) {
        MenuDecreasedEvent event = menuDecreasedEvent;
        System.out.println(
            "\n\n##### listener PaymentRequestPolicy : " +
            menuDecreasedEvent +
            "\n\n"
        );

        // Sample Logic //
        Payment.paymentRequestPolicy(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='MenuIncresedEvent'"
    )
    public void wheneverMenuIncresedEvent_PaymentCancelPolicy(
        @Payload MenuIncresedEvent menuIncresedEvent
    ) {
        MenuIncresedEvent event = menuIncresedEvent;
        System.out.println(
            "\n\n##### listener PaymentCancelPolicy : " +
            menuIncresedEvent +
            "\n\n"
        );

        // Sample Logic //
        Payment.paymentCancelPolicy(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
