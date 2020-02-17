package com.ningmeng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsCode;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.config.RabbitmqConfig;
import com.ningmeng.manage_cms.dao.CmsPageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by 1 on 2020/2/11.
 */
@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 发布页面方法
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId){
        boolean flag = createHtml();
        if(!flag){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        //查询数据库
        CmsPage cmsPage = this.findOne(pageId);
        if(cmsPage==null){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        Map<String,String> msgMap = new HashMap<String,String>();
        msgMap.put("pageId",pageId);
        //消息内容
        String msg = JSON.toJSONString(msgMap);
        //获取站点id作为routingKey
        String siteId = cmsPage.getSiteId();
        //发送json(pageId:"1") siteId 就是routingKey
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    /**
     * 创建静态页面
     */
    private boolean createHtml(){
        System.out.println("执行页面静态化程序，保存静态化文件完成。。。。");
        return true;
    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id){
        CmsPage one = this.findOne(id);
        if(one !=null){
            //删除
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    public CmsPage findOne(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
     * 修改方法  先查询对象是否存在  如果不存在 提示失败 存在的话进行修改
     * @param cmsPage
     * @return
     */
    public ResponseResult update(CmsPage cmsPage){
        CmsPage cmsPage1 = this.findOne(cmsPage.getPageId());
        if(cmsPage1!=null){
            cmsPageRepository.save(cmsPage);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 根据对象进行添加
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage){
        if(cmsPage==null){
            //向外抛异常 页面对象为空异常
            CustomExceptionCast.cast(CommonCode.FAIL);
        }


        //校验页面是否存在 根据页面名称  站点id  页面webpath查询
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cmsPage1 != null){
            CustomExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //不存在  添加
        cmsPage.setPageCreateTime(new Date());
        cmsPage.setPageId(null);//主键自增  spring data自动生成
        cmsPageRepository.save(cmsPage);
        //返回结果
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
    }


    /**
     * 站点id精准查询   模板id精准查询 页面别名模糊查询
     * @param page  起始页数
     * @param size  每页大小
     * @param queryPageRequest  查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        if(page <= 0){
            page = 1;
        }

        page = page - 1;//为了适应mongodb的接口将页码减 1
        //分页对象
        //分页查询
        PageRequest pageRequest = PageRequest.of(page,size);


        //构建条件值  后面要用
        CmsPage cmsPage = new CmsPage();
        //构建条件构建器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //站点id精准查询   模板id精准查询 页面别名模糊查询
        if(queryPageRequest.getPageAliase()!=null){
            exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(queryPageRequest.getSiteId()!=null){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(queryPageRequest.getTemplateId()!=null){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //构建条件
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);


        Page<CmsPage> all = cmsPageRepository.findAll(example,pageRequest);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<CmsPage>();
        cmsPageQueryResult.setList(all.getContent());
        cmsPageQueryResult.setTotal(all.getTotalElements());
        //返回结果
        return new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);

    }
}
