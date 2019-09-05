package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        Map map=new HashMap();

        /*Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        map.put("rows",page.getContent());*/

        //高亮显示
        HighlightQuery query = new SimpleHighlightQuery();

        //构建高亮选项对象
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//设置高亮的域
        highlightOptions.setSimplePrefix("<b><em style='color:red'>");//设置高亮前缀
        highlightOptions.setSimplePostfix("</em></b>");//设置高亮后缀
        query.setHighlightOptions(highlightOptions);//为查询对象设置高亮选项

        //关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //返回高亮页
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //高亮入口集合(每条记录的高亮)
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
        for (HighlightEntry<TbItem> entry : entryList) {
            //获取高亮列表(取决于高亮域的列数)
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();//之所以这高亮列是集合,是为了解决高亮域是多个的问题
            /*//可以不循环,直接将高亮列表放置在集合中
            for (HighlightEntry.Highlight h : highlightList) {
                List<String> sns = h.getSnipplets();//每个域有可能存贮多值
                System.out.println(sns);
            }*/

            //健壮性判断
            if (highlightList.size()>0 && highlightList.get(0).getSnipplets().size()>0){


                String title = highlightList.get(0).getSnipplets().get(0);//获得第一个域的第一个元素
                TbItem item = entry.getEntity();// entry.getEntity()与page.getContent()为同一引用
                item.setTitle(title);//将高亮字段设置进去,更改原来显示

            }




        }

        map.put("rows",page.getContent());

        return map;
    }
}
