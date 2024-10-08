package tableordering.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;
import tableordering.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentRepository
        extends PagingAndSortingRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);
}
