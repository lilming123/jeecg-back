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
import org.jeecg.modules.demo.base.entity.BaseMaterial;
import org.jeecg.modules.demo.base.service.IBaseMaterialService;

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
 * @Description: 原材料管理
 * @Author: jeecg-boot
 * @Date:   2023-01-23
 * @Version: V1.0
 */
@Api(tags="原材料管理")
@RestController
@RequestMapping("/base/baseMaterial")
@Slf4j
public class BaseMaterialController extends JeecgController<BaseMaterial, IBaseMaterialService> {
	@Autowired
	private IBaseMaterialService baseMaterialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param baseMaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "原材料管理-分页列表查询")
	@ApiOperation(value="原材料管理-分页列表查询", notes="原材料管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BaseMaterial>> queryPageList(BaseMaterial baseMaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BaseMaterial> queryWrapper = QueryGenerator.initQueryWrapper(baseMaterial, req.getParameterMap());
		Page<BaseMaterial> page = new Page<BaseMaterial>(pageNo, pageSize);
		IPage<BaseMaterial> pageList = baseMaterialService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param baseMaterial
	 * @return
	 */
	@AutoLog(value = "原材料管理-添加")
	@ApiOperation(value="原材料管理-添加", notes="原材料管理-添加")
	//@RequiresPermissions("base:base_material:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BaseMaterial baseMaterial) {
		baseMaterialService.save(baseMaterial);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param baseMaterial
	 * @return
	 */
	@AutoLog(value = "原材料管理-编辑")
	@ApiOperation(value="原材料管理-编辑", notes="原材料管理-编辑")
	//@RequiresPermissions("base:base_material:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BaseMaterial baseMaterial) {
		baseMaterialService.updateById(baseMaterial);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "原材料管理-通过id删除")
	@ApiOperation(value="原材料管理-通过id删除", notes="原材料管理-通过id删除")
	//@RequiresPermissions("base:base_material:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		baseMaterialService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "原材料管理-批量删除")
	@ApiOperation(value="原材料管理-批量删除", notes="原材料管理-批量删除")
	//@RequiresPermissions("base:base_material:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.baseMaterialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "原材料管理-通过id查询")
	@ApiOperation(value="原材料管理-通过id查询", notes="原材料管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BaseMaterial> queryById(@RequestParam(name="id",required=true) String id) {
		BaseMaterial baseMaterial = baseMaterialService.getById(id);
		if(baseMaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(baseMaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param baseMaterial
    */
    //@RequiresPermissions("base:base_material:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseMaterial baseMaterial) {
        return super.exportXls(request, baseMaterial, BaseMaterial.class, "原材料管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("base:base_material:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BaseMaterial.class);
    }

}
