package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CookStartedEvent extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Long userId;
    private Date createdAt;
    private String orderStatus;

    public CookStartedEvent(Shop aggregate) {
        super(aggregate);
    }

    public CookStartedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
