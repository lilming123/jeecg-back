package org.jeecg.modules.demo.storage.entity;

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
 * @Description: 采购入库
 * @Author: jeecg-boot
 * @Date:   2023-01-27
 * @Version: V1.0
 */
@Data
@TableName("storage_procurement")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="storage_procurement对象", description="采购入库")
public class StorageProcurement implements Serializable {
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
	/**材料id*/
	@Excel(name = "材料id", width = 15)
    @ApiModelProperty(value = "材料id")
    private String materialId;
	/**材料名*/
	@Excel(name = "材料名", width = 15)
    @ApiModelProperty(value = "材料名")
    private String name;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String specs;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private Integer num;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private String unit;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalMoney;
	/**仓库*/
	@Excel(name = "仓库", width = 15, dictTable = "storage", dicText = "name", dicCode = "id")
	@Dict(dictTable = "storage", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "仓库")
    private String storage;
	/**货架*/
	@Excel(name = "货架", width = 15, dictTable = "base_shelves", dicText = "code", dicCode = "id")
	@Dict(dictTable = "base_shelves", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "货架")
    private String shelves;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private String delFlag;
}
