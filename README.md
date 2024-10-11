# 하이오더
KT의 테이블오더 서비스 '하이오더'를 클라우드 네이티브 환경에서 MSA 전환하는 프로젝트입니다.


## I. 분석 및 설계

### 적합성 검토

|구분|적합도|비고|
|:------:|:---:|:---|
|비즈니스 민첩성|상|- 테이블 오더는 **경쟁이 치열한 시장** <br>- 배포 주기 단축으로 변화하는 고객 및 시장 **요구사항에 빠르게 대응** 필요|
|장애 격리|상|- **매출**과 직관련된 **대고객 서비스**로 장애 시 영향도가 매우 높음 <br>- 업무별 서비스 분리로 장애 전파 최소화 필요|
|확장성|중|- **주말 또는 특정 시간대**에 트래픽이 크게 증가함 <br>- 확장에 유리한 클라우드 네이티브 설계로 **유연한 트래픽 처리** 가능함|


### 조직 구성

* **AS-IS** (Horizontally-Aligned)
<p align="center">
	<img width="626" alt="org-asis" src="https://github.com/user-attachments/assets/c66e3937-0bb3-4831-9fc6-102d444ea9c7">
	<br><em>고객의 요구사항이 반영되기 어려운 조직 구성</em>
</p>

* **TO-BE** (Vertically-Aligned)
<p align="center">
	<img width="852" alt="ord-tobe" src="https://github.com/user-attachments/assets/2bd2fc60-010e-4ae5-9fdb-27a3ffc92eb2">
	<br><em>고객향 서비스가 가능한 도메인 업무 기반 조직 구성</em>
</p>

### 요구사항 검토

- **기능적 요구사항**
    - 사용자(고객/관리자)는 서비스에 로그인한다.
        - 계정 유형은 고객과 관리자로 나뉜다. `신규` 
        - 가게관리 페이지는 관리자 계정으로만 접근할 수 있다. `신규` 
    - 고객은 메뉴를 조회한다.
        - 주문 데이터를 바탕으로 추천 메뉴를 표시한다. `신규`
    - 고객은 메뉴를 장바구니에 담는다.
    - 고객은 장바구니에 담긴 메뉴를 주문한다.
        - 메뉴는 재고가 존재해야 주문이 승인된다. `신규` 
        - 메인 메뉴를 시켜야 사이드 메뉴를 주문할 수 있다. `신규` 
        - 특정 메뉴는 중복 주문을 할 수 없도록 막는다. (예: 이벤트성 할인 메뉴) `신규` 
	- 고객은 주문내역을 결제한다.
		- 간편결제 외부 API를 연동한다. `신규` 
    - 관리자는 매출 통계 기능을 통해 대시보드를 확인한다.
    - 관리자는 주문 현황 기능을 통해 주문을 확인하거나 취소한다. `신규`
    - 관리자는 가게관리 페이지에서 메뉴 카테고리 및 메뉴를 CRUD 한다. `신규` 
    - 관리자는 가게관리 페이지에서 주문 현황을 확인한다. `신규` 

- **비기능적 요구사항**
    - 트랜잭션
        - 결제가 완료되지 않은 주문은 전달되면 안된다.(Sync)
        - 재고가 존재하지 않는 메뉴는 결제되거나 재고가 감소하면 안된다.(보상 트랜잭션)
    - 장애격리
        - 가게관리 서비스 장애 시에도 주문 서비스는 가능해야한다. (Async, Event Driven)
        - 주문 서비스 과중되면 사용자를 잠시동안 받지 않고 주문을 잠시 후에 하도록 유도 (Circuit breaker, fallback)
    - 성능
        - 관리자는 전체 테이블, 고객은 현재 테이블 주문 이력을 프론트에서 확인할 수 있어야한다.(CQRS)

### 도메인 주도(DDD) 및 이벤트 기반(EDA) 설계

* **EventStorming**

    1. 기존 모놀리식 설계
        <p align="center">
	   <img width="926" alt="스크린샷 2024-10-11 오전 10 08 49" src="https://github.com/user-attachments/assets/46ebfc35-f93b-4c55-9767-1b2548b0e1ed" align="center">
            <br><em>단일 서버 내에 여러 도메인의 서비스를 운영</em>
        </p>

	2. 주요 이벤트 도출
        <p align="center">
            <img width="905" alt="table-ordering-only-events" src="https://github.com/user-attachments/assets/c0141820-4ee0-48ef-a13a-bcbf3d9d6382" align="center">
            <br><em>도메인 중심의 이벤트</em>
        </p>

    3. 완성된 1차 모형
        <p align="center">
            <img width="1110" alt="table-ordering-eventstorming" src="https://github.com/user-attachments/assets/f7fc81fa-7146-4c2f-ac4f-dd461738b5dc">
            <br><em>도메인별 바운디드 컨텍스트로 묶은 그림</em>
        </p>

    4. 결정된 최종 모형
	    <p align="center">
            <img width="1130" alt="table-ordering-eventstorming-5th" src="https://github.com/user-attachments/assets/09dc48cd-1b69-47b9-ba60-8daaf66cd367">
            <br><em>불필요한 요소 제거(예: Command가 아닌 로그인 기능 제외) 및 Aggregates 속성 보완</em>
        </p>

## II. 구현
* **도메인 주도 및 이벤트 기반 설계의 적용**

1. `Order`

```java
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

```

2. `Menu`
- Saga 기반 이벤트 드리븐 아키텍처
- 보상 트랜잭션(Compensation Transaction) 패턴 적용
    - 데이터베이스의 롤백으로 컨시스턴시를 맞추는게 아니라 보상 트랜잭션으로 데이터를 동기화
```java
@Entity
@Table(name = "menu", schema = "menu")
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

    private Long storeId;

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
        for (OrderMenu orderMenu : orderPlacedEvent.getOrderMenus()) {
            repository().findById(orderMenu.getMenuId()).ifPresent(menu -> {
                if (menu.getQty() >= orderMenu.getQty()) {
                    // 재고가 감소된 메뉴 리스트에 추가
                    decreasedMenus.add(menu);
                }
                // 재고 감소
                menu.setQty(menu.getQty() - orderMenu.getQty());
                repository().save(menu);
            });
        }

        // 주문한 메뉴 중에 재고가 없어서 감소하지 못했다면
        if (decreasedMenus.size() != orderPlacedEvent.getOrderMenus().size()) {
            // OutOfStockEvent 발행
            OutOfStockEvent outOfStockEvent = new OutOfStockEvent();
            outOfStockEvent.setOrderId(orderPlacedEvent.getId());
            outOfStockEvent.publishAfterCommit();
        }
        // 재고가 전부 감소했으면 MenuDecreasedEvent 한 번만 발행
        else if (decreasedMenus.size() == orderPlacedEvent.getOrderMenus().size()) {
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
        for (OrderMenu orderMenu : orderCancelledEvent.getOrderMenus()) {
            repository().findById(orderMenu.getMenuId()).ifPresent(menu -> {
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
```

3. `OrderMenu`
```java
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
```
4. `User`
```java
@Entity
@Table(name = "user", schema = "user")
@Data
// <<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Date createdAt;

    private Date updatedAt;

    @PostPersist
    public void onPostPersist() {
    }

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
                UserRepository.class);
        return userRepository;
    }
}
```
5. `Category`
```java
@Entity
@Table(name = "category", schema = "category")
@Data
// <<< DDD / Aggregate Root
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String categoryName;

    private Long storeId;

    @PostPersist
    public void onPostPersist() {
        CategoryCreatedEvent categoryCreatedEvent = new CategoryCreatedEvent(
                this);
        categoryCreatedEvent.publishAfterCommit();

        CategoryDeletedEvent categoryDeletedEvent = new CategoryDeletedEvent(
                this);
        categoryDeletedEvent.publishAfterCommit();
    }

    public static CategoryRepository repository() {
        CategoryRepository categoryRepository = CategoryApplication.applicationContext.getBean(
                CategoryRepository.class);
        return categoryRepository;
    }
}
// >>> DDD / Aggregate Root
```

6. `Payment`
```java
@Entity
@Table(name = "payment", schema = "payment")
@Data
// <<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer paymentAmount;

    private Long userId;

    private Long orderId;

    private String paymentStatus;

    @PostPersist
    public void onPostPersist() {
        PaymentCompleteEvent paymentCompleteEvent = new PaymentCompleteEvent(
                this);
        paymentCompleteEvent.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate() {
        switch (this.paymentStatus) {
            case "Cancelled":
                PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(
                        this);
                paymentCancelledEvent.publishAfterCommit();
                break;

            default:
                break;
        }

    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
                PaymentRepository.class);
        return paymentRepository;
    }

    // <<< Clean Arch / Port Method
    public static void paymentRequestPolicy(MenuDecreasedEvent menuDecreasedEvent) {
        // implement business logic here:
        Payment payment = new Payment();
        payment.setOrderId(menuDecreasedEvent.getOrderId());
        payment.setUserId(menuDecreasedEvent.getUserId());
        payment.setPaymentAmount(menuDecreasedEvent.getPaymentAmount());
        payment.setPaymentStatus("Completed");
        repository().save(payment);
    }

    // >>> Clean Arch / Port Method
    // <<< Clean Arch / Port Method
    public static void paymentCancelPolicy(MenuIncresedEvent menuIncresedEvent) {
        // implement business logic here:
        repository().findByOrderId(menuIncresedEvent.getOrderId()).ifPresent(payment -> {
            payment.setPaymentStatus("Cancelled");
            repository().save(payment);
        });

    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
```


## III. 운영

* **k8s 클러스터 구성**
```bash
$ kubectl get all

NAME                              READY   STATUS    RESTARTS   AGE
pod/category-79d947cfdd-kcjxk     1/1     Running   0          76m
pod/frontend-7ffbbcdf98-9mc7n     1/1     Running   0          45m
pod/management-84567f5f9c-vdgms   1/1     Running   0          76m
pod/menu-849cd8d48b-zczj4         1/1     Running   0          70m
pod/my-kafka-0                    1/1     Running   0          21m
pod/order-7d8b759c75-ft4gm        1/1     Running   0          61s
pod/order-7d8b759c75-q64wg        1/1     Running   0          61s
pod/order-7d8b759c75-v2wkr        1/1     Running   0          2m27s
pod/payment-55c44cfc8b-5qqcn      1/1     Running   0          76m
pod/postgres-695c7fdf88-trdql     1/1     Running   0          76m
pod/siege                         1/1     Running   0          4m39s
pod/user-545f7b8477-bfj4t         1/1     Running   0          76m

NAME                        TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)                      AGE
service/category            ClusterIP   10.0.54.174    <none>        8080/TCP                     76m
service/frontend            ClusterIP   10.0.70.187    <none>        8080/TCP                     76m
service/kubernetes          ClusterIP   10.0.0.1       <none>        443/TCP                      77m
service/management          ClusterIP   10.0.164.176   <none>        8080/TCP                     76m
service/menu                ClusterIP   10.0.211.207   <none>        8080/TCP                     76m
service/my-kafka            ClusterIP   10.0.171.84    <none>        9092/TCP                     21m
service/my-kafka-headless   ClusterIP   None           <none>        9092/TCP,9094/TCP,9093/TCP   21m
service/order               ClusterIP   10.0.1.34      <none>        8080/TCP                     76m
service/payment             ClusterIP   10.0.237.15    <none>        8080/TCP                     76m
service/postgres-service    ClusterIP   10.0.21.5      <none>        5432/TCP                     76m
service/user                ClusterIP   10.0.151.91    <none>        8080/TCP                     76m

NAME                         READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/category     1/1     1            1           76m
deployment.apps/frontend     1/1     1            1           76m
deployment.apps/management   1/1     1            1           76m
deployment.apps/menu         1/1     1            1           76m
deployment.apps/order        3/3     3            3           76m
deployment.apps/payment      1/1     1            1           76m
deployment.apps/postgres     1/1     1            1           76m
deployment.apps/user         1/1     1            1           76m

NAME                                    DESIRED   CURRENT   READY   AGE
replicaset.apps/category-79d947cfdd     1         1         1       76m
replicaset.apps/frontend-7c69fb6f9b     0         0         0       76m
replicaset.apps/frontend-7ffbbcdf98     1         1         1       45m
replicaset.apps/frontend-bfd749d55      0         0         0       47m
replicaset.apps/management-84567f5f9c   1         1         1       76m
replicaset.apps/menu-567f5b69f7         0         0         0       76m
replicaset.apps/menu-849cd8d48b         1         1         1       70m
replicaset.apps/order-7b85d5c94d        0         0         0       76m
replicaset.apps/order-7d8b759c75        3         3         3       2m28s
replicaset.apps/payment-55c44cfc8b      1         1         1       76m
replicaset.apps/postgres-695c7fdf88     1         1         1       76m
replicaset.apps/user-545f7b8477         1         1         1       76m

NAME                        READY   AGE
statefulset.apps/my-kafka   1/1     21m

NAME                                        REFERENCE          TARGETS    MINPODS   MAXPODS   REPLICAS   AGE
horizontalpodautoscaler.autoscaling/order   Deployment/order   303%/50%   1         3         3          4m17s
```

* **Ingress 설정**
```bash
$ kubectl describe ingress

Name:             hiorder-ingress
Labels:           <none>
Namespace:        default
Address:          20.214.195.16
Ingress Class:    nginx
Default backend:  <default>
Rules:
  Host        Path  Backends
  ----        ----  --------
  *           
              /              frontend:8080 (10.244.1.246:8080)
              /orders        order:8080 (10.244.0.100:8080)
              /menus         menu:8080 (10.244.0.195:8080)
              /categories    category:8080 (10.244.0.102:8080)
              /managements   management:8080 (10.244.2.185:8080)
              /payments      payment:8080 (10.244.0.53:8080)
              /users         user:8080 (10.244.1.204:8080)
Annotations:  ingressclass.kubernetes.io/is-default-class: true
              nginx.ingress.kubernetes.io/ssl-redirect: false
Events:       <none>
```

* **Autoscaler**

```bash
# order 서비스 트래픽 증가 시 리소스 증가 설정

apiVersion: apps/v1
kind: Deployment
...
    spec:
      containers:
        - name: order
          image: jangsoowang/order:v1
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: "200m"
```

```bash
# siege로 order 서비스에 부하 적용 후 결과

Transactions:                   2906 hits
Availability:                  73.93 %
Elapsed time:                  26.29 secs
Data transferred:              15.02 MB
Response time:                  0.18 secs
Transaction rate:             110.54 trans/sec
Throughput:                     0.57 MB/sec
Concurrency:                   19.68
Successful transactions:        2906
Failed transactions:            1025
Longest transaction:            1.27
Shortest transaction:           0.03
```

```bash
$ kubectl get po -w
NAME                          READY   STATUS    RESTARTS   AGE
...
order-7d8b759c75-ft4gm        1/1     Running   0          25s
order-7d8b759c75-q64wg        1/1     Running   0          25s
order-7d8b759c75-v2wkr        1/1     Running   0          111s
...
```

```bash
$ kubectl get hpa
NAME    REFERENCE          TARGETS    MINPODS   MAXPODS   REPLICAS   AGE
order   Deployment/order   153%/50%   1         3         3          4m1s
```

* **Config Map**
    - Config Map 목록
    
    `$ kubectl get configmap`

    ```bash
    NAME               DATA   AGE
    init-sql-config    1      17h
    kube-root-ca.crt   1      3d1h
    my-kafka-scripts   1      17h
    ```

    - postgresql: 초기화 시 실행

    `$ kubectl get configmap init-sql-config -o yaml`

    ```yaml
    apiVersion: v1
    data:
    init.sql: "(내용 생략)"
    kind: ConfigMap
    metadata:
    creationTimestamp: "2024-10-10T07:53:05Z"
    name: init-sql-config
    namespace: default
    resourceVersion: "839369"
    uid: dde923d9-3958-4eec-b5f5-abeaf3ee3a65
    ```
* **Secret**

```yaml
# postgres deployment의 pass가 노출되어 secret 적용
...
env:
            - name: POSTGRES_DB
              value: 'postgres'
            - name: POSTGRES_USER
              value: 'postgres'
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: postgres-secret  # Secret 이름
                  key: POSTGRES_PASSWORD  # Secret에서 참조할 키
```

```bash
$ kubectl get secret postgres-secret -o yaml

apiVersion: v1
data:
  POSTGRES_PASSWORD: YWRtaW4=
kind: Secret
metadata:
  creationTimestamp: "2024-10-11T03:21:58Z"
  name: postgres-secret
  namespace: default
  resourceVersion: "1133713"
  uid: 9b272af9-1e17-4880-9c79-244bd57973f8
type: Opaque
```

* **Grafana & Prometheus**

<p align="center">
    <img width="1280" alt="grafana" src="https://github.com/user-attachments/assets/3c54cf5c-a1ad-4344-898b-f4865023e419" align="center">
</p>
<p align="center">
<img width="1280" alt="grafana2" src="https://github.com/user-attachments/assets/0477707d-9214-4ae0-b3d5-bddbdfff968d" align="center">
    <br><em>그라파나 대시보드 접속 화면</em>
</p>
쿠버네티스 리소스 모니터링을 위한 그라파나 및 프로메테우스 설정.