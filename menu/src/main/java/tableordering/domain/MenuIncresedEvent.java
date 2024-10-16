package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class MenuIncresedEvent extends AbstractEvent {

    private Long orderId;
    private Long userId;
    private int paymentAmount;

    public MenuIncresedEvent() {
        super();
    }
}
// >>> DDD / Domain Event
