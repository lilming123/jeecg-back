package org.jeecg.modules.demo.procurement.service;

import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购材料明细表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
public interface IProcurementOrderItemService extends IService<ProcurementOrderItem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ProcurementOrderItem>
	 */
	public List<ProcurementOrderItem> selectByMainId(String mainId);
}
