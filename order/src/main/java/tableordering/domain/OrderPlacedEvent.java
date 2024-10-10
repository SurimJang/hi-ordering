package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderPlacedEvent extends AbstractEvent {

    private Long id;
    private Long userId;
    private List<OrderMenu> orderMenus;
    private Integer qty;
    private Date createdAt;
    private Date updatedAt;
    private String orderStatus;

    public OrderPlacedEvent(Order aggregate) {
        super(aggregate);
    }

    public OrderPlacedEvent() {
        super();
    }
}
// >>> DDD / Domain Event
