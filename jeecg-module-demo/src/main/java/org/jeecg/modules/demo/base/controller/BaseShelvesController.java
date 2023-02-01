package org.jeecg.modules.demo.base.controller;

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
import org.jeecg.modules.demo.base.entity.BaseShelves;
import org.jeecg.modules.demo.base.service.IBaseShelvesService;

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
 * @Description: 货架管理
 * @Author: jeecg-boot
 * @Date:   2023-01-27
 * @Version: V1.0
 */
@Api(tags="货架管理")
@RestController
@RequestMapping("/base/baseShelves")
@Slf4j
public class BaseShelvesController extends JeecgController<BaseShelves, IBaseShelvesService> {
	@Autowired
	private IBaseShelvesService baseShelvesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param baseShelves
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "货架管理-分页列表查询")
	@ApiOperation(value="货架管理-分页列表查询", notes="货架管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BaseShelves>> queryPageList(BaseShelves baseShelves,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BaseShelves> queryWrapper = QueryGenerator.initQueryWrapper(baseShelves, req.getParameterMap());
		Page<BaseShelves> page = new Page<BaseShelves>(pageNo, pageSize);
		IPage<BaseShelves> pageList = baseShelvesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param baseShelves
	 * @return
	 */
	@AutoLog(value = "货架管理-添加")
	@ApiOperation(value="货架管理-添加", notes="货架管理-添加")
	//@RequiresPermissions("base:base_shelves:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BaseShelves baseShelves) {
		baseShelvesService.save(baseShelves);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param baseShelves
	 * @return
	 */
	@AutoLog(value = "货架管理-编辑")
	@ApiOperation(value="货架管理-编辑", notes="货架管理-编辑")
	//@RequiresPermissions("base:base_shelves:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BaseShelves baseShelves) {
		baseShelvesService.updateById(baseShelves);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "货架管理-通过id删除")
	@ApiOperation(value="货架管理-通过id删除", notes="货架管理-通过id删除")
	//@RequiresPermissions("base:base_shelves:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		baseShelvesService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "货架管理-批量删除")
	@ApiOperation(value="货架管理-批量删除", notes="货架管理-批量删除")
	//@RequiresPermissions("base:base_shelves:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.baseShelvesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "货架管理-通过id查询")
	@ApiOperation(value="货架管理-通过id查询", notes="货架管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BaseShelves> queryById(@RequestParam(name="id",required=true) String id) {
		BaseShelves baseShelves = baseShelvesService.getById(id);
		if(baseShelves==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(baseShelves);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param baseShelves
    */
    //@RequiresPermissions("base:base_shelves:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseShelves baseShelves) {
        return super.exportXls(request, baseShelves, BaseShelves.class, "货架管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("base:base_shelves:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BaseShelves.class);
    }

}
