package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderCancelledEvent extends AbstractEvent {

    private Long id;
    private Long userId;
    private List<Long> menuId;
    private Integer qty;
    private Date createdAt;
    private Date updatedAt;
    private String orderStatus;

    public OrderCancelledEvent(Order aggregate) {
        super(aggregate);
    }

    public OrderCancelledEvent() {
        super();
    }
}
//>>> DDD / Domain Event
