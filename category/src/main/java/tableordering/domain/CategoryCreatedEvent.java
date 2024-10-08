package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CategoryCreatedEvent extends AbstractEvent {

    private Long id;
    private String categoryName;

    public CategoryCreatedEvent(Category aggregate) {
        super(aggregate);
    }

    public CategoryCreatedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
