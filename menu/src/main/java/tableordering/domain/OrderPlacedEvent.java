package tableordering.domain;

import java.util.*;
import lombok.*;
import tableordering.domain.*;
import tableordering.infra.AbstractEvent;

@Data
@ToString
public class OrderPlacedEvent extends AbstractEvent {

    private Long id;
    private Long userId;
    private Object menuId;
    private Integer qty;
    private Date createdAt;
    private Date updatedAt;
    private String orderStatus;
}
