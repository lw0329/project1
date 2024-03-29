package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		//获取商家id
		String sellId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(sellId);//设置商家id

		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){

		/*校检是否是当前商家的id*/
		//1.根据前台传来的id获取数据库中当前的商品
		Goods goods2 = goodsService.findOne(goods.getGoods().getId());

		//2.获取当前商家的id
		String sellId = SecurityContextHolder.getContext().getAuthentication().getName();

		//3.如果传来的商品的id与当前用户的id不对应,则属于非法操作;如果查找到的商品id与当前用户不对应,也属于非法操作
		if (!goods2.getGoods().getSellerId().equals(sellId) || !goods.getGoods().getSellerId().equals(sellId)){
			return new Result(false,"操作非法");
		}


		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){

		//获取商家id
		String sellId = SecurityContextHolder.getContext().getAuthentication().getName();

		goods.setSellerId(sellId);

		return goodsService.findPage(goods, page, rows);

	}


	@RequestMapping("/isMarketable")
	public Result isMarketable(Long[] ids,String status){


		try {
			goodsService.isMarketable(ids, status);
			return new Result(true,"操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"操作失败");
		}

	}



	
}
