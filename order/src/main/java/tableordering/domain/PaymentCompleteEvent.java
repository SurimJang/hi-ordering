package tableordering.domain;

import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

@Data
@ToString
public class PaymentCompleteEvent extends AbstractEvent {

    private Long id;
    private Integer paymentAmount;
    private Long userId;
    private Long orderId;
    private String paymentStatus;
}
