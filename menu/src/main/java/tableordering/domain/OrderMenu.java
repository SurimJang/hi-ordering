package tableordering.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Embeddable
@Data
public class OrderMenu {

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    public OrderMenu() {
    }

    public OrderMenu(Long menuId, Integer qty) {
        this.menuId = menuId;
        this.qty = qty;
    }

    // Getters and Setters
    // 생략
}