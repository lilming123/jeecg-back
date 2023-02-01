package org.jeecg.modules.demo.procurement.mapper;

import java.util.List;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购材料明细表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
public interface ProcurementOrderItemMapper extends BaseMapper<ProcurementOrderItem> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<ProcurementOrderItem>
   */
	public List<ProcurementOrderItem> selectByMainId(@Param("mainId") String mainId);
}
