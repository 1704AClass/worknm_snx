package com.ningmeng.manage_cms;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Created by 1 on 2020/2/11.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest{

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void findAll(){
        //问题：它知道查哪个表吗？   查的是CmsPage
        List<CmsPage> list =  cmsPageRepository.findAll();
        for (CmsPage cmspage:list) {
           System.out.println(cmspage.getPageName());
        }
    }

    //分页查询方法
    @Test
    public void testFindPage(){
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        System.out.println(all);
    }

    //添加
    @Test
    public void testInsert(){
        //定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("1");
        cmsPage.setTemplateId("1");
        cmsPage.setPageName("测试页面");
        cmsPage.setDataUrl("1");
        cmsPage.setPageId("1");
        //如果这个地方写null  代表让mongo帮自动生成id
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);

    }

    //删除
    @Test
    public void testDelete(){

        //cmsPageRepository.deleteById("5e4294302ccb6a1e001dbfc7");
        Query query = Query.query(Criteria.where("_id").is("5e4294302ccb6a1e001dbfc7"));
        mongoTemplate.remove(query,"cms_page");
    }

    //修改
    @Test
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5af942190e661827d8e2f5e3");
        // optional.isPresent()  false 为空  true不为空
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面01");
            cmsPageRepository.save(cmsPage);
        }
    }

    //自定义查询
    @Test
    public void testFindByName(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CmsPage> all = cmsPageRepository.findByPageName("测试页面",pageRequest);
        System.out.println(all.getContent());
    }

    @Test
    public void testFindAll(){
        //条件匹配器查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //页面名称模糊查询，需要自定义字符串的匹配器来实现模糊查询
        exampleMatcher = exampleMatcher.withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());

        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.startsWith());
        //条件值
        CmsPage cmsPage = new CmsPage();
        //栈点id
        //cmsPage.setSiteId("");
        //模板id
        //cmsPage.setTemplateId("");

        //名称
        cmsPage.setPageName("页面");
        cmsPage.setPageAliase("页面");
        //创建了一个条件实例
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        PageRequest pageRequest = PageRequest.of(0,10);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageRequest);

        System.out.println(all.getContent());

    }

}
