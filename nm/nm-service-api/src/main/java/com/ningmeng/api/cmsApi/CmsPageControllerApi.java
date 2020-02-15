package com.ningmeng.api.cmsApi;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;

/*

 */
public interface CmsPageControllerApi {

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过ID查询页面")
    public CmsPage findById(String id);

    @ApiOperation("修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("通过ID删除页面")
    public ResponseResult delete(String id);



}
