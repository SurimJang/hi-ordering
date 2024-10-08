package tableordering.domain;

import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

@Data
@ToString
public class MenuDecreasedEvent extends AbstractEvent {
    private Long orderId;
    private Long userId;
    private int paymentAmount;

}
