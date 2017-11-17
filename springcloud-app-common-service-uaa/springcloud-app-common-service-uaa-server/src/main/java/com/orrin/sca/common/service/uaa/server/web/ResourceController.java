package com.orrin.sca.common.service.uaa.server.web;

import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.common.service.uaa.client.feignclient.SysResourceServiceApi;
import com.orrin.sca.common.service.uaa.client.service.SysResourcesService;
import com.orrin.sca.common.service.uaa.client.vo.ResourceBriefInfo;
import com.orrin.sca.common.service.uaa.client.vo.ResourceMoreInfo;
import com.orrin.sca.common.service.uaa.server.dao.SysResourcesRepository;
import com.orrin.sca.component.menu.MenuModel;
import com.orrin.sca.component.privilege.annotation.ResourcePrivilegeEntity;
import com.orrin.sca.component.utils.string.LocalStringUtils;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController implements SysResourceServiceApi {

    @Autowired
    private SysResourcesRepository sysResourcesRepository;

    @Autowired
    private SysResourcesService sysResourcesService;

    @Override
    @RequestMapping(path = "/feign/addprivilege", method = RequestMethod.POST)
    public ResponseResult<SysResourcesEntity> insertPrivilege(@RequestBody ResourcePrivilegeEntity resourcePrivilegeEntity) {
        ResponseResult<SysResourcesEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        int count = sysResourcesRepository.countByGlobalUniqueId(resourcePrivilegeEntity.getGlobalUniqueId());

        if(count > 0){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("globalUniqueId repeate ! : " + "resourcePrivilege.resourceGlobalUniqueId()");
            return responseResult;
        }

        SysResourcesEntity sysResourcesEntity = new SysResourcesEntity();
        sysResourcesEntity.setGlobalUniqueId(resourcePrivilegeEntity.getGlobalUniqueId());
        sysResourcesEntity.setResourceDesc(resourcePrivilegeEntity.getResourceDesc());
        sysResourcesEntity.setResourcePath(resourcePrivilegeEntity.getResourcePath());
        sysResourcesEntity.setResourceName(resourcePrivilegeEntity.getResourceName());
        sysResourcesEntity.setEnable(true);


        sysResourcesEntity = sysResourcesService.saveAndFlush(sysResourcesEntity);

        responseResult.setData(sysResourcesEntity);

        return responseResult;
    }

    @Override
    @RequestMapping(path = "/feign/authresources", method = RequestMethod.GET)
    public List<ResourceBriefInfo> findAuthResources() {
        ResponseResult<List<ResourceBriefInfo>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");

        List<ResourceBriefInfo> list = new ArrayList<>();

        List<Object[]> result  = sysResourcesRepository.findAuthResources();
        Iterator<Object[]> it = result.iterator();

        while(it.hasNext()){
            Object[] o = it.next();
            //HashMap<String,String> map = new HashMap<String,String>();
            String resourcePaths = (String)o[0];
            if(resourcePaths.contains(",")){
                String resourcePath[] = resourcePaths.split(",");
                for(String rp : resourcePath) {
                    ResourceBriefInfo resourceBriefInfo = new ResourceBriefInfo();
                    resourceBriefInfo.setResourcePath(rp);
                    resourceBriefInfo.setAuthorityMark((String)o[1]);
                    resourceBriefInfo.setGlobalUniqueId((String)o[3]);
                    resourceBriefInfo.setRequestMethod((String)o[4]);
                    list.add(resourceBriefInfo);
                }
            }else {
                ResourceBriefInfo resourceBriefInfo = new ResourceBriefInfo();
                resourceBriefInfo.setResourcePath(resourcePaths);
                resourceBriefInfo.setAuthorityMark((String)o[1]);
                resourceBriefInfo.setGlobalUniqueId((String)o[3]);
                resourceBriefInfo.setRequestMethod((String)o[4]);
                list.add(resourceBriefInfo);
            }

        }

        responseResult.setData(list);
        return list;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseResult<SysResourcesEntity> insert(@RequestBody SysResourcesEntity resourcesEntity) {
        ResponseResult<SysResourcesEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        int count = sysResourcesRepository.countByGlobalUniqueId(resourcesEntity.getGlobalUniqueId());

        if(!StringUtils.hasText(resourcesEntity.getResourceId())){
            resourcesEntity.setResourceId(LocalStringUtils.uuidLowerCase());
        }else {
            count--;
        }

        if(count > 0){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("globalUniqueId repeate ! : " + "resourcePrivilege.resourceGlobalUniqueId()");
            return responseResult;
        }

        resourcesEntity = sysResourcesService.saveAndFlush(resourcesEntity);

        responseResult.setData(resourcesEntity);

        return responseResult;
    }


    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public ResponseResult<Page<SysResourcesEntity>> list() {
        ResponseResult<Page<SysResourcesEntity>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        Pageable pageable = new PageRequest(0, 100000, Sort.Direction.ASC, "priority");
        Page<SysResourcesEntity> resourcesPage = sysResourcesRepository.findAll(pageable);
        responseResult.setData(resourcesPage);

        return responseResult;
    }

    @RequestMapping(path = "/{resourceId}", method = RequestMethod.DELETE)
    public ResponseResult<Void> delete(@PathVariable("resourceId") String resourceId) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        SysResourcesEntity sysResourcesEntityQuery = new  SysResourcesEntity();
        sysResourcesEntityQuery.setFatherResourceId(resourceId);
        sysResourcesEntityQuery.setEnable(null);
        sysResourcesEntityQuery.setIssys(null);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("fatherResourceId", match -> match.ignoreCase(false));
        Example<SysResourcesEntity> entityExample = Example.of(sysResourcesEntityQuery, exampleMatcher);
        long deleteCheck = sysResourcesRepository.count(entityExample);

        if(deleteCheck > 0) {
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg(" this resource has children !");
            return responseResult;
        }

        sysResourcesService.delete(resourceId);

        return responseResult;
    }

    @RequestMapping(path = "/menu", method = RequestMethod.GET)
    public ResponseResult<List<MenuModel>> menu() {
        ResponseResult<List<MenuModel>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        List<MenuModel> menuModels = sysResourcesService.wrapMenu();
        responseResult.setData(menuModels);

        return responseResult;
    }

    @RequestMapping(path = "/moreinfo/{resourceId}", method = RequestMethod.GET)
    public ResponseResult<ResourceMoreInfo> moreinfo(@PathVariable("resourceId") String resourceId) {
        ResponseResult<ResourceMoreInfo> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        SysResourcesEntity mainResource = sysResourcesRepository.findOne(resourceId);
        ResourceMoreInfo resourceMoreInfo = new ResourceMoreInfo();
        resourceMoreInfo.setMainResource(mainResource);

        SysResourcesEntity sysResourcesEntityQuery = new  SysResourcesEntity();
        sysResourcesEntityQuery.setResourceType("URL");
        sysResourcesEntityQuery.setFatherResourceId(resourceId);
        sysResourcesEntityQuery.setEnable(null);
        sysResourcesEntityQuery.setIssys(null);


        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("resourceType", match -> match.ignoreCase(false))
                .withMatcher("fatherResourceId", match -> match.ignoreCase(false))
                ;
        Example<SysResourcesEntity> entityExample = Example.of(sysResourcesEntityQuery, exampleMatcher);
        List<SysResourcesEntity> contentResources = sysResourcesRepository.findAll(entityExample);
        resourceMoreInfo.setContentResources(contentResources);

        responseResult.setData(resourceMoreInfo);

        return responseResult;
    }
}
