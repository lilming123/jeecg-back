package org.jeecg.modules.demo.produce.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 生产流程管理
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@ApiModel(value="produce_process对象", description="生产流程管理")
@Data
@TableName("produce_process")
public class ProduceProcess implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**流程名*/
	@Excel(name = "流程名", width = 15)
    @ApiModelProperty(value = "流程名")
    private String name;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15, dictTable = "base_goods", dicText = "name", dicCode = "id")
    @Dict(dictTable = "base_goods", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "产品名称")
    private String goodsName;
	/**标准工时(小时)*/
	@Excel(name = "标准工时(小时)", width = 15)
    @ApiModelProperty(value = "标准工时(小时)")
    private java.math.BigDecimal time;
	/**末级工作小组*/
	@Excel(name = "末级工作小组", width = 15, dictTable = "worker_group", dicText = "name", dicCode = "id")
    @Dict(dictTable = "worker_group", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "末级工作小组")
    private String workerGroup;
	/**生产顺序*/
	@Excel(name = "生产顺序", width = 15)
    @ApiModelProperty(value = "生产顺序")
    private Integer precedence;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private String delFlag;
}
