package org.jeecg.modules.demo.produce.service;

import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产流程材料表
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
public interface IProduceProcessMaterialService extends IService<ProduceProcessMaterial> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ProduceProcessMaterial>
	 */
	public List<ProduceProcessMaterial> selectByMainId(String mainId);
}
