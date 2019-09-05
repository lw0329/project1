package com.pinyougou.solrutil;


import com.alibaba.fastjson.JSON;
import com.pinyougou.dao.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;


    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入商品数据
     */

    public void importItemData(){
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//将已启用的sku
        List<TbItem> itemList = itemMapper.selectByExample(example);
        System.out.println("=====商品列表=====");

        for (TbItem item : itemList) {
            System.out.println(item.getTitle());
            Map map = JSON.parseObject(item.getSpec(), Map.class);//将spec字段的字符串解析为json

            item.setSpecMap(map);

        }


        System.out.println("=====结束=====");

        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    public static void main(String[] args) {

        ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");

        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");

        solrUtil.importItemData();


    }
}
