package org.jeecg.modules.demo.procurement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.procurement.entity.ProcurementPrice;
import org.jeecg.modules.demo.procurement.service.IProcurementPriceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 采购价格管理
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Api(tags="采购价格管理")
@RestController
@RequestMapping("/procurement/procurementPrice")
@Slf4j
public class ProcurementPriceController extends JeecgController<ProcurementPrice, IProcurementPriceService> {
	@Autowired
	private IProcurementPriceService procurementPriceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param procurementPrice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购价格管理-分页列表查询")
	@ApiOperation(value="采购价格管理-分页列表查询", notes="采购价格管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProcurementPrice>> queryPageList(ProcurementPrice procurementPrice,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProcurementPrice> queryWrapper = QueryGenerator.initQueryWrapper(procurementPrice, req.getParameterMap());
		Page<ProcurementPrice> page = new Page<ProcurementPrice>(pageNo, pageSize);
		IPage<ProcurementPrice> pageList = procurementPriceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param procurementPrice
	 * @return
	 */
	@AutoLog(value = "采购价格管理-添加")
	@ApiOperation(value="采购价格管理-添加", notes="采购价格管理-添加")
	//@RequiresPermissions("procurement:procurement_price:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProcurementPrice procurementPrice) {
		procurementPriceService.save(procurementPrice);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param procurementPrice
	 * @return
	 */
	@AutoLog(value = "采购价格管理-编辑")
	@ApiOperation(value="采购价格管理-编辑", notes="采购价格管理-编辑")
	//@RequiresPermissions("procurement:procurement_price:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProcurementPrice procurementPrice) {
		procurementPriceService.updateById(procurementPrice);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购价格管理-通过id删除")
	@ApiOperation(value="采购价格管理-通过id删除", notes="采购价格管理-通过id删除")
	//@RequiresPermissions("procurement:procurement_price:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		procurementPriceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购价格管理-批量删除")
	@ApiOperation(value="采购价格管理-批量删除", notes="采购价格管理-批量删除")
	//@RequiresPermissions("procurement:procurement_price:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.procurementPriceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购价格管理-通过id查询")
	@ApiOperation(value="采购价格管理-通过id查询", notes="采购价格管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProcurementPrice> queryById(@RequestParam(name="id",required=true) String id) {
		ProcurementPrice procurementPrice = procurementPriceService.getById(id);
		if(procurementPrice==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(procurementPrice);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param procurementPrice
    */
    //@RequiresPermissions("procurement:procurement_price:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProcurementPrice procurementPrice) {
        return super.exportXls(request, procurementPrice, ProcurementPrice.class, "采购价格管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("procurement:procurement_price:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ProcurementPrice.class);
    }

}
