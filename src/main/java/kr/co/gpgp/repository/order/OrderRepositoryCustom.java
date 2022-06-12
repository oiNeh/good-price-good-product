package kr.co.gpgp.repository.order;

import kr.co.gpgp.domain.order.OrderSearchCondition;
import kr.co.gpgp.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    Page<Order> orderSearch(OrderSearchCondition condition, Pageable pageable);

}