package tableordering.domain;

import tableordering.domain.OrderPlacedEvent;
import tableordering.domain.OrderCancelledEvent;
import tableordering.domain.OrderConfirmedEvent;
import tableordering.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;

@Entity
@Table(name = "\"order\"", schema = "\"order\"")
@Data

// <<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private Long userId;

    @ElementCollection
    @CollectionTable(name = "order_menus", // 테이블 이름
            schema = "\"order\"", // 스키마 이름
            joinColumns = @JoinColumn(name = "order_id") // 조인 컬럼 지정
    )
    @Column(name = "menu_id")
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String orderStatus;

    private Integer paymentAmount;

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @PostPersist
    public void onPostPersist() {
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(this);
        orderPlacedEvent.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        // 주문 상태에 따라 각각 이벤트 발행
        switch (this.orderStatus) {
            case "OrderCancelled":
                OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(this);
                orderCancelledEvent.publishAfterCommit();
                break;

            case "OrderConfirmed":
                OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(this);
                orderConfirmedEvent.publishAfterCommit();
                break;

            // 필요한 다른 상태 이벤트 추가 가능
        }
    }

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }

    // <<< Clean Arch / Port Method
    public static void updateStatusPolicy(OutOfStockEvent outOfStockEvent) {

        // 재고 부족으로 인한 주문 취소
        repository().findById(outOfStockEvent.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("OrderCancelled");
            repository().save(order);
        });
    }

    // >>> Clean Arch / Port Method
    // <<< Clean Arch / Port Method
    public static void updateStatusPolicy(PaymentCompleteEvent paymentCompleteEvent) {

        // 결제 완료시 주문 생성 및 완료
        repository().findById(paymentCompleteEvent.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("OrderConfirmed");
            repository().save(order);
        });
    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
