package com.ningmeng.manage_cms.dao;

import com.ningmeng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Created by 1 on 2020/2/11.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String>{

    // 自定义查询 根据页面名称查询
    public Page<CmsPage> findByPageName(String pageName, Pageable pageable);

    //根据页面名称  站点id   页面访问webpath先查询   是否存在该页面
    public CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
