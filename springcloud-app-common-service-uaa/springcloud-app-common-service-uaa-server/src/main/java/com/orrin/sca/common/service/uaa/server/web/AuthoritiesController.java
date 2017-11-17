package com.orrin.sca.common.service.uaa.server.web;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysAuthoritiesResourcesService;
import com.orrin.sca.common.service.uaa.client.vo.AuthoritiesAndResources;
import com.orrin.sca.common.service.uaa.client.vo.AuthoritiesRequestParams;
import com.orrin.sca.common.service.uaa.server.core.secure.URLFilterInvocationSecurityMetadataSource;
import com.orrin.sca.common.service.uaa.server.dao.SysAuthoritiesRepository;
import com.orrin.sca.component.jpa.dao.Range;
import com.orrin.sca.component.utils.string.LocalStringUtils;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/authority")
public class AuthoritiesController {

    @Autowired
    private SysAuthoritiesRepository sysAuthoritiesRepository;

    @Autowired
    private SysAuthoritiesResourcesService sysAuthoritiesResourcesService;

    @Autowired
    private URLFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public ResponseResult<Page<SysAuthoritiesEntity>> list(@RequestBody AuthoritiesRequestParams authoritiesRequestParams) {
        ResponseResult<Page<SysAuthoritiesEntity>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");


        SysAuthoritiesEntity queryEntity = new  SysAuthoritiesEntity();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();

        if(StringUtils.hasText(authoritiesRequestParams.getAuthorityMark())){
            queryEntity.setAuthorityMark(authoritiesRequestParams.getAuthorityMark());
            exampleMatcher.withMatcher("authorityMark", match -> match.contains());
        }

        if(StringUtils.hasText(authoritiesRequestParams.getMessage())){
            queryEntity.setMessage(authoritiesRequestParams.getMessage());
            exampleMatcher.withMatcher("message", match -> match.contains());
        }

        if(StringUtils.hasText(authoritiesRequestParams.getAuthorityName())){
            queryEntity.setAuthorityName(authoritiesRequestParams.getAuthorityName());
            exampleMatcher.withMatcher("authorityName", match -> match.contains());
        }

        Example<SysAuthoritiesEntity> entityExample = Example.of(queryEntity, exampleMatcher);

        com.orrin.sca.component.jpa.dao.Range<SysAuthoritiesEntity> rangeCreatedDate = new com.orrin.sca.component.jpa.dao.Range<>("createdDate", authoritiesRequestParams.getCreatedDateStart(),authoritiesRequestParams.getCreatedDateEnd());

        List<Range<SysAuthoritiesEntity>> rangeList = new ArrayList<>();
        rangeList.add(rangeCreatedDate);


        Pageable pageable = new PageRequest(authoritiesRequestParams.getQueryPage(), authoritiesRequestParams.getSize(), Sort.Direction.ASC, "authorityId");
        Page<SysAuthoritiesEntity> resourcesPage = sysAuthoritiesRepository.queryByExampleWithRange(entityExample, rangeList, pageable);
        responseResult.setData(resourcesPage);

        return responseResult;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseResult<SysAuthoritiesEntity> insertOrEdit(@RequestBody SysAuthoritiesEntity sysAuthoritiesEntity) {
        ResponseResult<SysAuthoritiesEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        if(sysAuthoritiesEntity.getAuthorityMark() == null || !sysAuthoritiesEntity.getAuthorityMark().startsWith("AUTH_")){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("authorityMark must start with 'AUTH_' !");
            return responseResult;
        }

        if(!StringUtils.hasText(sysAuthoritiesEntity.getAuthorityId())) {
            sysAuthoritiesEntity.setAuthorityId(LocalStringUtils.uuidLowerCase());
        }

        SysAuthoritiesEntity saeCheck = sysAuthoritiesRepository.findByAuthorityMark(sysAuthoritiesEntity.getAuthorityMark());

        if(saeCheck != null && !saeCheck.getAuthorityId().equals(sysAuthoritiesEntity.getAuthorityId())){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("authorityMark repeat !");
            return responseResult;
        }

        sysAuthoritiesEntity = sysAuthoritiesRepository.saveAndFlush(sysAuthoritiesEntity);
        responseResult.setData(sysAuthoritiesEntity);

        return responseResult;
    }

    @RequestMapping(path = "/{authorityId}", method = RequestMethod.DELETE)
    public ResponseResult<Void> delete(@PathVariable("authorityId") String authorityId) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        long countCheck = sysAuthoritiesResourcesService.countByAuthorityId(authorityId);

        if(countCheck > 0){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg(" authority has " + countCheck + " resources, please delete resources under authority first !");
            return responseResult;
        }

        sysAuthoritiesRepository.delete(authorityId);

        return responseResult;
    }

    @RequestMapping(path = "/andresource/{authorityId}/{resourceId}", method = RequestMethod.DELETE)
    public ResponseResult<Void> delete(@PathVariable("authorityId") String authorityId, @PathVariable("resourceId") String resourceId) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        sysAuthoritiesResourcesService.deleteByAuthorityIdAndResourceId(authorityId, resourceId);
        urlFilterInvocationSecurityMetadataSource.refreshResuorceMap();

        return responseResult;
    }

    @RequestMapping(path = "/{authorityId}", method = RequestMethod.GET)
    public ResponseResult<SysAuthoritiesEntity> getSysAuthoritiesEntity(@PathVariable("authorityId") String authorityId) {
        ResponseResult<SysAuthoritiesEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        SysAuthoritiesEntity sysAuthoritiesEntity = sysAuthoritiesRepository.findOne(authorityId);

        if(sysAuthoritiesEntity == null){
            sysAuthoritiesEntity = sysAuthoritiesRepository.findByAuthorityMark(authorityId);
        }

        if(sysAuthoritiesEntity == null){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("could not find authority entity by authorityId or authorityMark");
            return responseResult;
        }

        responseResult.setData(sysAuthoritiesEntity);

        return responseResult;
    }

    @RequestMapping(path = "/andresource/{authorityId}", method = RequestMethod.GET)
    public ResponseResult<AuthoritiesAndResources> getSysAuthoritiesAndResources(@PathVariable("authorityId") String authorityId, HttpServletRequest request) {

        String resourceName = request.getParameter("resourceName");
        String page = request.getParameter("page");
        String size = request.getParameter("size");

        if(!StringUtils.hasText(resourceName)){
            resourceName = null;
        }

        if(!StringUtils.hasText(page)){
            page = "0";
        }

        if(!StringUtils.hasText(size)){
            size = "10";
        }

        int queryPage = Integer.parseInt(page);
        queryPage = queryPage > 0 ? (queryPage-1):queryPage;

        ResponseResult<AuthoritiesAndResources> responseResult = sysAuthoritiesResourcesService.findAuthoritiesAndResources(authorityId,resourceName, queryPage, Integer.parseInt(size));
        return responseResult;
    }

    @RequestMapping(path = "/andresource/{authorityId}", method = RequestMethod.POST)
    public ResponseResult<Void> addSysAuthoritiesAndResources(@PathVariable("authorityId") String authorityId, @RequestBody List<SysResourcesEntity> resources, HttpServletRequest request) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        sysAuthoritiesResourcesService.addResourcesUnderAuthoritiy(authorityId, resources);
        urlFilterInvocationSecurityMetadataSource.refreshResuorceMap();

        return responseResult;
    }

    @RequestMapping(path = "/andresource/not/{authorityId}", method = RequestMethod.GET)
    public ResponseResult<Page<SysResourcesEntity>> getResourcesNotUnderAuthority(@PathVariable("authorityId") String authorityId, HttpServletRequest request) {

        String resourceName = request.getParameter("resourceName");
        String page = request.getParameter("page");
        String size = request.getParameter("size");

        if(!StringUtils.hasText(resourceName)){
            resourceName = null;
        }

        if(!StringUtils.hasText(page)){
            page = "0";
        }

        if(!StringUtils.hasText(size)){
            size = "10";
        }

        int queryPage = Integer.parseInt(page);
        queryPage = queryPage > 0 ? (queryPage-1):queryPage;

        Page<SysResourcesEntity> resourcesEntities = sysAuthoritiesResourcesService.findResourcesNotUnderAuthoritiy(authorityId,resourceName, queryPage, Integer.parseInt(size));
        ResponseResult<Page<SysResourcesEntity>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");
        responseResult.setData(resourcesEntities);

        return responseResult;
    }
}
