package tableordering.domain;

import tableordering.domain.MenuDecreasedEvent;
import tableordering.domain.MenuIncresedEvent;
import tableordering.domain.MenuCreatedEvent;
import tableordering.domain.MenuDeletedEvent;
import tableordering.domain.OutOfStockEvent;
import tableordering.MenuApplication;
import javax.persistence.*;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;

@Entity
@Table(name = "Menu_table")
@Data

// <<< DDD / Aggregate Root
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String menuName;

    private Integer menuPrice;

    private Integer qty;

    private Long categoryId;

    @PostPersist
    public void onPostPersist() {
        MenuCreatedEvent menuCreatedEvent = new MenuCreatedEvent(this);
        menuCreatedEvent.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove() {
        MenuDeletedEvent menuDeletedEvent = new MenuDeletedEvent(this);
        menuDeletedEvent.publishAfterCommit();
    }

    public static MenuRepository repository() {
        MenuRepository menuRepository = MenuApplication.applicationContext.getBean(MenuRepository.class);
        return menuRepository;
    }

    // <<< Clean Arch / Port Method
    public static void decreaseMenuPolicy(OrderPlacedEvent orderPlacedEvent) {
        List<Menu> decreasedMenus = new ArrayList<>(); // 재고가 감소된 메뉴 리스트

        // 메뉴 재고 검사
        for (Menu orderMenu : orderPlacedEvent.getMenuId()) {
            repository().findById(orderMenu.getId()).ifPresent(menu -> {
                if (menu.getQty() >= orderMenu.getQty()) {
                    // 재고 감소
                    menu.setQty(menu.getQty() - orderMenu.getQty());
                    repository().save(menu);

                    // 재고가 감소된 메뉴 리스트에 추가
                    decreasedMenus.add(menu);
                }
            });
        }

        // 주문한 메뉴 중에 재고가 없어서 감소하지 못했다면
        if (decreasedMenus.size() != orderPlacedEvent.getMenuId().size()) {
            // OutOfStockEvent 발행
            OutOfStockEvent outOfStockEvent = new OutOfStockEvent();
            outOfStockEvent.setOrderId(orderPlacedEvent.getId());
            outOfStockEvent.publishAfterCommit();
        }
        // 재고가 전부 감소했으면 MenuDecreasedEvent 한 번만 발행
        else if (decreasedMenus.size() == orderPlacedEvent.getMenuId().size()) {
            MenuDecreasedEvent menuDecreasedEvent = new MenuDecreasedEvent();
            menuDecreasedEvent.setOrderId(orderPlacedEvent.getId());
            menuDecreasedEvent.setUserId(orderPlacedEvent.getUserId());
            menuDecreasedEvent.setPaymentAmount(orderPlacedEvent.getPaymentAmount());
            menuDecreasedEvent.publishAfterCommit();
        }
    }

    // >>> Clean Arch / Port Method
    // <<< Clean Arch / Port Method
    public static void increaseMenuPolicy(OrderCancelledEvent orderCancelledEvent) {
        //
        for (Menu orderMenu : orderCancelledEvent.getMenuId()) {
            repository().findById(orderMenu.getId()).ifPresent(menu -> {
                menu.setQty(menu.getQty() + orderMenu.getQty());
                repository().save(menu);
            });
        }
        MenuIncresedEvent menuIncresedEvent = new MenuIncresedEvent();
        menuIncresedEvent.setOrderId(orderCancelledEvent.getId());
        menuIncresedEvent.setUserId(orderCancelledEvent.getUserId());
        menuIncresedEvent.setPaymentAmount(orderCancelledEvent.getPaymentAmount());
        menuIncresedEvent.publishAfterCommit();
    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
