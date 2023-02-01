package org.jeecg.modules.demo.procurement.service;

import org.jeecg.modules.demo.procurement.entity.ProcurementOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生成采购订单
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
public interface IProcurementOrderService extends IService<ProcurementOrder> {


    public void orderItem(List<ProcurementOrder> procurementOrderList);
}
