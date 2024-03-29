package com.pinyougou.sellergoods.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.dao.*;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;


    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Autowired
    private TbGoodsDescMapper goodsDescMapper;


    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        goods.getGoods().setAuditStatus("0");//状态,未审核
        goods.getGoods().setIsMarketable("1");//状态,默认上架
        goodsMapper.insert(goods.getGoods());//插入商品基本信息

        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());//将商品基本表的id给商品扩展表
        goodsDescMapper.insert(goods.getGoodsDesc());//插入商品扩展描述信息

        saveItemList(goods);


    }



    private void setItemValues(Goods goods,TbItem item){

        item.setGoodsId(goods.getGoods().getId());//商品SPU编号
        item.setSellerId(goods.getGoods().getSellerId());//商家编号
        item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号（3级）
        item.setCreateTime(new Date());//创建日期
        item.setUpdateTime(new Date());//修改日期

        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());

        //分类名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());

        //商家名称_店铺名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(seller.getNickName());

        //图片地址 获取spu的第一个图片地址
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);

        if (imageList.size() > 0) {
            item.setImage(imageList.get(0).get("url").toString());
        }

    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {

        goods.getGoods().setAuditStatus("0");//设置审核状态,如果经过修改,需要将审核状态重新设置为0

        goodsMapper.updateByPrimaryKey(goods.getGoods());//保存商品基本表

        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());//保存商品扩展信息表

        //删除原有sku表---itemList
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);

        //添加新的sku列表
        saveItemList(goods);

    }



    /**
     * 创建私有方法，插入sku列表数据
     * @param goods
     */
    private void saveItemList(Goods goods){

        if ("1".equals(goods.getGoods().getIsEnableSpec())) {


            for (TbItem item : goods.getItemList()) {
                //构建标题 SPU名称+规格选项值
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> specMap = JSON.parseObject(item.getSpec());
//			for (String key : specMap.keySet()) {
//				title +=" "+specMap.get(key);
//			}

                Collection<Object> values = specMap.values();
                for (Object value : values) {
                    title += " " + value;
                }


                item.setTitle(title);
                setItemValues(goods,item);


                itemMapper.insert(item);


            }

        }else{

            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());//设置标题
            item.setPrice(goods.getGoods().getPrice());//设置价格
            item.setStatus("1");//设置状态
            item.setIsDefault("1");//设置默认
            item.setNum(99999);//设置默认数量
            item.setSpec("{}");//设置规格数组

            setItemValues(goods,item);

            itemMapper.insert(item);


        }

    }


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {

        Goods goods =new Goods();

        //查询基本信息
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);

        //相同主键,故通过主键查询描述表
        TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(goodsDesc);

        //商品SKU商品列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);

        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {

            //逻辑删除
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(goods);

        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();//非删除状态

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                //将模糊查询修改为精确匹配,避免模糊查询查到别人的商品
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(goods);
        }
    }

    @Override
    public void isMarketable(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods goods = goodsMapper.selectByPrimaryKey(id);
            goods.setIsMarketable(status);
            goodsMapper.updateByPrimaryKey(goods);
        }
    }

}
