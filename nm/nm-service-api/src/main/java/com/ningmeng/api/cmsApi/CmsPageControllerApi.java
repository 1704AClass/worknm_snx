package com.ningmeng.api.cmsapi;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by 1 on 2020/2/11.
 * 有两个作用：1.约束Controller 2向外发布接口使用
 */
@Api(value = "cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询页面列表")//描述一个类的一个方法，或者说一个接口
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "size",value = "每页记录数",required = true,paramType = "path",dataType = "int",defaultValue = "10")
    })
    //QueryResponseResult 自定义返回对象  QueryPageRequest自定义参数对象
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过id查询页面")
    public CmsPage findOne(String id);

    @ApiOperation("修改页面")
    public ResponseResult update(CmsPage cmsPage);

    @ApiOperation("删除页面")
    public ResponseResult delete(String id);

    @ApiOperation(("发布页面"))
    public ResponseResult post(String pageId);

}
