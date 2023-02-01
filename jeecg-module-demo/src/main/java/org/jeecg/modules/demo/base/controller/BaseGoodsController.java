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
import org.jeecg.modules.demo.base.entity.BaseGoods;
import org.jeecg.modules.demo.base.service.IBaseGoodsService;

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
 * @Description: 商品管理
 * @Author: jeecg-boot
 * @Date:   2023-01-23
 * @Version: V1.0
 */
@Api(tags="商品管理")
@RestController
@RequestMapping("/base/baseGoods")
@Slf4j
public class BaseGoodsController extends JeecgController<BaseGoods, IBaseGoodsService> {
	@Autowired
	private IBaseGoodsService baseGoodsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param baseGoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "商品管理-分页列表查询")
	@ApiOperation(value="商品管理-分页列表查询", notes="商品管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BaseGoods>> queryPageList(BaseGoods baseGoods,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BaseGoods> queryWrapper = QueryGenerator.initQueryWrapper(baseGoods, req.getParameterMap());
		Page<BaseGoods> page = new Page<BaseGoods>(pageNo, pageSize);
		IPage<BaseGoods> pageList = baseGoodsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param baseGoods
	 * @return
	 */
	@AutoLog(value = "商品管理-添加")
	@ApiOperation(value="商品管理-添加", notes="商品管理-添加")
	//@RequiresPermissions("base:base_goods:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BaseGoods baseGoods) {
		baseGoodsService.save(baseGoods);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param baseGoods
	 * @return
	 */
	@AutoLog(value = "商品管理-编辑")
	@ApiOperation(value="商品管理-编辑", notes="商品管理-编辑")
	//@RequiresPermissions("base:base_goods:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BaseGoods baseGoods) {
		baseGoodsService.updateById(baseGoods);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品管理-通过id删除")
	@ApiOperation(value="商品管理-通过id删除", notes="商品管理-通过id删除")
	//@RequiresPermissions("base:base_goods:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		baseGoodsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商品管理-批量删除")
	@ApiOperation(value="商品管理-批量删除", notes="商品管理-批量删除")
	//@RequiresPermissions("base:base_goods:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.baseGoodsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "商品管理-通过id查询")
	@ApiOperation(value="商品管理-通过id查询", notes="商品管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BaseGoods> queryById(@RequestParam(name="id",required=true) String id) {
		BaseGoods baseGoods = baseGoodsService.getById(id);
		if(baseGoods==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(baseGoods);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param baseGoods
    */
    //@RequiresPermissions("base:base_goods:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseGoods baseGoods) {
        return super.exportXls(request, baseGoods, BaseGoods.class, "商品管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("base:base_goods:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BaseGoods.class);
    }

}
