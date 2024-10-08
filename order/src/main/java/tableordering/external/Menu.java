package tableordering.external;

import java.util.Date;
import lombok.Data;

@Data
public class Menu {

    private Long id;
    private String menuName;
    private Integer menuPrice;
    private Integer qty;
    private Long categoryId;
}
