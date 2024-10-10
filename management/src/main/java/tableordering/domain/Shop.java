package tableordering.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import tableordering.ManagementApplication;
import tableordering.domain.CookStartedEvent;

@Entity
@Table(name = "sales")
@Data
// <<< DDD / Aggregate Root
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long orderId;

    private Long userId;

    private Date createdAt;

    private String orderStatus;

    @PostPersist
    public void onPostPersist() {
        CookStartedEvent cookStartedEvent = new CookStartedEvent(this);
        cookStartedEvent.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        CookCancelledEvent cookCancelledEvent = new CookCancelledEvent(this);
        cookCancelledEvent.publishAfterCommit();
    }

    public static ShopRepository repository() {
        ShopRepository shopRepository = ManagementApplication.applicationContext.getBean(
                ShopRepository.class);
        return shopRepository;
    }

    // <<< Clean Arch / Port Method
    public static void startCookPolicy(OrderConfirmedEvent orderConfirmedEvent) {
        // implement business logic here:
        Shop shop = new Shop();
        shop.setOrderId(orderConfirmedEvent.getId());
        shop.setCreatedAt(orderConfirmedEvent.getCreatedAt());
        shop.setOrderStatus(orderConfirmedEvent.getOrderStatus());
        shop.setUserId(orderConfirmedEvent.getUserId());
        repository().save(shop);
    }

    // <<< Clean Arch / Port Method
    public static void cancelCookPolicy(OrderCancelledEvent orderCancelledEvent) {
        // implement business logic here:
        repository().findByOrderId(orderCancelledEvent.getId()).ifPresent(shop -> {
            shop.setOrderStatus(orderCancelledEvent.getOrderStatus());
        });
    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
