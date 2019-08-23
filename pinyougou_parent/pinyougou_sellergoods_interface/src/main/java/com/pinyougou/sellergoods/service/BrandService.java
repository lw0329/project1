package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;


/**
 * 品牌服务层接口
 */
public interface BrandService {

    /**
     * 返回全部列表
     * @return
     */
    public List<TbBrand> findAll();


    /**
     * 返回分页列表
     */
    public PageResult findPage(int pageNum,int pageSize);


    /**
     * 增加
     * @param brand
     */
    public void add(TbBrand brand);

}
