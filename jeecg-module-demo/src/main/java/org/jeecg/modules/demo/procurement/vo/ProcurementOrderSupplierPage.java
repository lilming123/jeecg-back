package org.jeecg.modules.demo.procurement.vo;

import java.util.List;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderSupplier;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购审批表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
@Data
@ApiModel(value="procurement_order_supplierPage对象", description="采购审批表")
public class ProcurementOrderSupplierPage {

	/**主键*/
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
	/**供应商名*/
	@Excel(name = "供应商名", width = 15)
	@ApiModelProperty(value = "供应商名")
    private String supplier;
	/**供应商地址*/
	@Excel(name = "供应商地址", width = 15)
	@ApiModelProperty(value = "供应商地址")
    private String location;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
	@ApiModelProperty(value = "联系人")
    private String person;
	/**电话*/
	@Excel(name = "电话", width = 15)
	@ApiModelProperty(value = "电话")
    private String phone;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
	@ApiModelProperty(value = "总金额")
    private java.math.BigDecimal totalMoney;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "state")
    @Dict(dicCode = "state")
	@ApiModelProperty(value = "状态")
    private String state;
	/**发票类型*/
	@Excel(name = "发票类型", width = 15, dicCode = "invoice")
    @Dict(dicCode = "invoice")
	@ApiModelProperty(value = "发票类型")
    private String invoice;
	/**付款方式*/
	@Excel(name = "付款方式", width = 15, dicCode = "payment_way")
    @Dict(dicCode = "payment_way")
	@ApiModelProperty(value = "付款方式")
    private String paymentWay;
	/**运输方式*/
	@Excel(name = "运输方式", width = 15, dicCode = "tran_way")
    @Dict(dicCode = "tran_way")
	@ApiModelProperty(value = "运输方式")
    private String tranWay;
	/**交货日期*/
	@Excel(name = "交货日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "交货日期")
    private Date deliveryTime;
	/**结算方式*/
	@Excel(name = "结算方式", width = 15, dicCode = "settlement_way")
    @Dict(dicCode = "settlement_way")
	@ApiModelProperty(value = "结算方式")
    private String settlementWay;
	/**结算日期*/
	@Excel(name = "结算日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "结算日期")
    private Date settlement;

	@ExcelCollection(name="采购材料明细表")
	@ApiModelProperty(value = "采购材料明细表")
	private List<ProcurementOrderItem> procurementOrderItemList;

}
