package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class MenuDecreasedEvent extends AbstractEvent {

    private Long orderId;
    private Long userId;
    private int paymentAmount;

    public MenuDecreasedEvent() {
        super();
    }
}
// >>> DDD / Domain Event
