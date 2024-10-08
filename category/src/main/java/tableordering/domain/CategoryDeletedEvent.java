package tableordering.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CategoryDeletedEvent extends AbstractEvent {

    private Long id;
    private String categoryName;

    public CategoryDeletedEvent(Category aggregate) {
        super(aggregate);
    }

    public CategoryDeletedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
