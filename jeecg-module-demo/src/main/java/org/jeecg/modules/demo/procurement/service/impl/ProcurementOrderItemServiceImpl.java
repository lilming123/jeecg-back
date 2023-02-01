package org.jeecg.modules.demo.procurement.service.impl;

import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import org.jeecg.modules.demo.procurement.mapper.ProcurementOrderItemMapper;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderItemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购材料明细表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
@Service
public class ProcurementOrderItemServiceImpl extends ServiceImpl<ProcurementOrderItemMapper, ProcurementOrderItem> implements IProcurementOrderItemService {
	
	@Autowired
	private ProcurementOrderItemMapper procurementOrderItemMapper;
	
	@Override
	public List<ProcurementOrderItem> selectByMainId(String mainId) {
		return procurementOrderItemMapper.selectByMainId(mainId);
	}
}
