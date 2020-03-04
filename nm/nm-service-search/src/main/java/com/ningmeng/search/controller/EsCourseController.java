package com.ningmeng.search.controller;

import com.ningmeng.api.course.EsCourseControllerApi;
import com.ningmeng.framework.domain.search.CourseSearchParam;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.search.service.EsCourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Resource
    private EsCourseService esCourseService;

    @Override
    @GetMapping(value = "/list/{page}/{size}")
    public QueryResponseResult list(@PathVariable("page") int page,@PathVariable("size") int size,@RequestBody CourseSearchParam courseSearchParam) throws IOException {
        return esCourseService.list(page,size,courseSearchParam);
    }
}
