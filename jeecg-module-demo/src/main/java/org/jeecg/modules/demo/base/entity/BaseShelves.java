package org.jeecg.modules.demo.base.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 货架管理
 * @Author: jeecg-boot
 * @Date:   2023-01-27
 * @Version: V1.0
 */
@Data
@TableName("base_shelves")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="base_shelves对象", description="货架管理")
public class BaseShelves implements Serializable {
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
	/**货架号*/
	@Excel(name = "货架号", width = 15)
    @ApiModelProperty(value = "货架号")
    private String code;
	/**所属仓库*/
	@Excel(name = "所属仓库", width = 15, dictTable = "storage", dicText = "name", dicCode = "id")
	@Dict(dictTable = "storage", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属仓库")
    private String storageId;
	/**货架层数*/
	@Excel(name = "货架层数", width = 15)
    @ApiModelProperty(value = "货架层数")
    private String num;
	/**长度(单位米)*/
	@Excel(name = "长度(单位米)", width = 15)
    @ApiModelProperty(value = "长度(单位米)")
    private BigDecimal length;
	/**宽度(单位米)*/
	@Excel(name = "宽度(单位米)", width = 15)
    @ApiModelProperty(value = "宽度(单位米)")
    private BigDecimal width;
	/**高度(单位米)*/
	@Excel(name = "高度(单位米)", width = 15)
    @ApiModelProperty(value = "高度(单位米)")
    private BigDecimal height;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
}
