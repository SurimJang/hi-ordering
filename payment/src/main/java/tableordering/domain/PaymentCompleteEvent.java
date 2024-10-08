package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentCompleteEvent extends AbstractEvent {

    private Long id;
    private Integer paymentAmount;
    private Long userId;
    private Long orderId;
    private String paymentStatus;

    public PaymentCompleteEvent(Payment aggregate) {
        super(aggregate);
    }

    public PaymentCompleteEvent() {
        super();
    }
}
//>>> DDD / Domain Event
