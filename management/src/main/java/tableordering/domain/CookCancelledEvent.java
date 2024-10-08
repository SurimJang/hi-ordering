package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CookCancelledEvent extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Long userId;
    private Date createdAt;
    private String orderStatus;

    public CookCancelledEvent(Shop aggregate) {
        super(aggregate);
    }

    public CookCancelledEvent() {
        super();
    }
}
// >>> DDD / Domain Event
