package org.jeecg.modules.demo.produce.mapper;

import java.util.List;
import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产流程材料表
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
public interface ProduceProcessMaterialMapper extends BaseMapper<ProduceProcessMaterial> {

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
   * @return List<ProduceProcessMaterial>
   */
	public List<ProduceProcessMaterial> selectByMainId(@Param("mainId") String mainId);
}
