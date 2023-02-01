package org.jeecg.modules.demo.procurement.service;

import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderSupplier;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购审批表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
public interface IProcurementOrderSupplierService extends IService<ProcurementOrderSupplier> {

	/**
	 * 添加一对多
	 *
	 * @param procurementOrderSupplier
	 * @param procurementOrderItemList
	 */
	public void saveMain(ProcurementOrderSupplier procurementOrderSupplier,List<ProcurementOrderItem> procurementOrderItemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param procurementOrderSupplier
   * @param procurementOrderItemList
	 */
	public void updateMain(ProcurementOrderSupplier procurementOrderSupplier,List<ProcurementOrderItem> procurementOrderItemList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
