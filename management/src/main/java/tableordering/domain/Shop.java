package tableordering.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import tableordering.ManagementApplication;
import tableordering.domain.CookStartedEvent;

@Entity
@Table(name = "Shop_table")
@Data
//<<< DDD / Aggregate Root
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

    public static ShopRepository repository() {
        ShopRepository shopRepository = ManagementApplication.applicationContext.getBean(
            ShopRepository.class
        );
        return shopRepository;
    }

    //<<< Clean Arch / Port Method
    public static void startCookPolicy(
        OrderConfirmedEvent orderConfirmedEvent
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Shop shop = new Shop();
        repository().save(shop);

        CookStartedEvent cookStartedEvent = new CookStartedEvent(shop);
        cookStartedEvent.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderConfirmedEvent.get???()).ifPresent(shop->{
            
            shop // do something
            repository().save(shop);

            CookStartedEvent cookStartedEvent = new CookStartedEvent(shop);
            cookStartedEvent.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
