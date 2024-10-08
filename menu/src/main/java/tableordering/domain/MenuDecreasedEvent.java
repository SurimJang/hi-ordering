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

    private Long id;
    private String menuName;
    private Integer menuPrice;
    private Integer qty;
    private Long categoryId;

    public MenuDecreasedEvent(Menu aggregate) {
        super(aggregate);
    }

    public MenuDecreasedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
