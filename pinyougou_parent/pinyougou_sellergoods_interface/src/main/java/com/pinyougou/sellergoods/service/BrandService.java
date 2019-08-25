package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;


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


    /**
     * 根据id查找对象实体
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);


    /**
     * 修改
     * @param brand
     */
    public void update(TbBrand brand);


    /**
     * 批量删除
     * @param ids
     */
    public void delete(Long[] ids);


    /**
     * 分页条件查询
     * @param brand 查询条件封装的对象
     * @param pageNum 当前页
     * @param pageSize 每页显示条数
     * @return
     */
    public PageResult findPage(TbBrand brand,int pageNum,int pageSize);


    /**
     * 在模板下拉列表中显示所有的品牌
     * @return
     */
    public List<Map> selectOptionList();

}
