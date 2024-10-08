package tableordering.domain;

import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

@Data
@ToString
public class MenuIncresedEvent extends AbstractEvent {

    private Long id;
    private String menuName;
    private Integer menuPrice;
    private Integer qty;
    private Long categoryId;
}
