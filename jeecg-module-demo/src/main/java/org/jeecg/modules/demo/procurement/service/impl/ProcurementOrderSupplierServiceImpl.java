package org.jeecg.modules.demo.procurement.service.impl;

import org.jeecg.modules.demo.procurement.entity.ProcurementOrderSupplier;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import org.jeecg.modules.demo.procurement.mapper.ProcurementOrderItemMapper;
import org.jeecg.modules.demo.procurement.mapper.ProcurementOrderSupplierMapper;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderSupplierService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购审批表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
@Service
public class ProcurementOrderSupplierServiceImpl extends ServiceImpl<ProcurementOrderSupplierMapper, ProcurementOrderSupplier> implements IProcurementOrderSupplierService {

	@Autowired
	private ProcurementOrderSupplierMapper procurementOrderSupplierMapper;
	@Autowired
	private ProcurementOrderItemMapper procurementOrderItemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ProcurementOrderSupplier procurementOrderSupplier, List<ProcurementOrderItem> procurementOrderItemList) {
		procurementOrderSupplierMapper.insert(procurementOrderSupplier);
		if(procurementOrderItemList!=null && procurementOrderItemList.size()>0) {
			for(ProcurementOrderItem entity:procurementOrderItemList) {
				//外键设置
				entity.setSupplier(procurementOrderSupplier.getSupplier());
				procurementOrderItemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(ProcurementOrderSupplier procurementOrderSupplier,List<ProcurementOrderItem> procurementOrderItemList) {
		BigDecimal totalMoney = new BigDecimal(0);
		if(procurementOrderItemList!=null && procurementOrderItemList.size()>0) {
			for (ProcurementOrderItem eachProcurementOrderItem : procurementOrderItemList) {
				totalMoney = totalMoney.add(eachProcurementOrderItem.getPrice().multiply(new BigDecimal(eachProcurementOrderItem.getNum())));
			}
		}
		procurementOrderSupplier.setTotalMoney(totalMoney);

		procurementOrderSupplierMapper.updateById(procurementOrderSupplier);
		
		//1.先删除子表数据
		procurementOrderItemMapper.deleteByMainId(procurementOrderSupplier.getId());
		
		//2.子表数据重新插入
		if(procurementOrderItemList!=null && procurementOrderItemList.size()>0) {
			for(ProcurementOrderItem entity:procurementOrderItemList) {
				entity.setTotalMoney(entity.getPrice().multiply(new BigDecimal(entity.getNum())));
				//外键设置
				entity.setSupplier(procurementOrderSupplier.getId());
				procurementOrderItemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		procurementOrderItemMapper.deleteByMainId(id);
		procurementOrderSupplierMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			procurementOrderItemMapper.deleteByMainId(id.toString());
			procurementOrderSupplierMapper.deleteById(id);
		}
	}
	
}
