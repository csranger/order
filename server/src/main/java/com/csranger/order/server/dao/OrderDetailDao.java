package com.csranger.order.server.dao;

import com.csranger.order.server.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailDao extends JpaRepository<OrderDetail, String> {


}
