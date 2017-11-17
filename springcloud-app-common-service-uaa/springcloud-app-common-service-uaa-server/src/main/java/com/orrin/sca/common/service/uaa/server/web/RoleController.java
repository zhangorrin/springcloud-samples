package com.orrin.sca.common.service.uaa.server.web;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysRolesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysRolesAuthoritiesService;
import com.orrin.sca.common.service.uaa.client.vo.RoleAndAuthorities;
import com.orrin.sca.common.service.uaa.client.vo.RoleAuthorityRequestParams;
import com.orrin.sca.common.service.uaa.client.vo.RoleRequestParams;
import com.orrin.sca.common.service.uaa.server.dao.SysRolesRepository;
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
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private SysRolesRepository sysRolesRepository;

    @Autowired
    private SysRolesAuthoritiesService sysRolesAuthoritiesService;

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public ResponseResult<Page<SysRolesEntity>> list(@RequestBody RoleRequestParams roleRequestParams) {
        ResponseResult<Page<SysRolesEntity>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        SysRolesEntity queryRolesEntity = new  SysRolesEntity();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();

        if(StringUtils.hasText(roleRequestParams.getRoleName())){
            queryRolesEntity.setRoleName(roleRequestParams.getRoleName());
            exampleMatcher.withMatcher("roleName", match -> match.contains());
        }

        if(StringUtils.hasText(roleRequestParams.getRoleDesc())){
            queryRolesEntity.setRoleDesc(roleRequestParams.getRoleDesc());
            exampleMatcher.withMatcher("roleDesc", match -> match.contains());
        }


        Example<SysRolesEntity> entityExample = Example.of(queryRolesEntity, exampleMatcher);

        Range<SysRolesEntity> rangeCreatedDate = new Range<>("createdDate", roleRequestParams.getCreatedDateStart(),roleRequestParams.getCreatedDateEnd());

        List<Range<SysRolesEntity>> rangeList = new ArrayList<>();
        rangeList.add(rangeCreatedDate);


        Pageable pageable = new PageRequest(roleRequestParams.getQueryPage(), roleRequestParams.getSize(), Sort.Direction.ASC, "roleId");
        Page<SysRolesEntity> resourcesPage = sysRolesRepository.queryByExampleWithRange(entityExample, rangeList, pageable);
        responseResult.setData(resourcesPage);

        return responseResult;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseResult<SysRolesEntity> insertOrEdit(@RequestBody SysRolesEntity sysRolesEntity) {
        ResponseResult<SysRolesEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        if(sysRolesEntity.getRoleName() == null || !sysRolesEntity.getRoleName().startsWith("ROLE_")){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("roleName must start with 'ROLE_' !");
            return responseResult;
        }

        if(!StringUtils.hasText(sysRolesEntity.getRoleId())) {
            sysRolesEntity.setRoleId(LocalStringUtils.uuidLowerCase());
        }

        SysRolesEntity roleCheck = sysRolesRepository.findByRoleName(sysRolesEntity.getRoleName());

        if(roleCheck != null && !roleCheck.getRoleId().equals(sysRolesEntity.getRoleId())){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("roleName exists !");
            return responseResult;
        }

        sysRolesEntity = sysRolesRepository.saveAndFlush(sysRolesEntity);
        responseResult.setData(sysRolesEntity);

        return responseResult;
    }

    @RequestMapping(path = "/{roleId}", method = RequestMethod.DELETE)
    public ResponseResult<Void> delete(@PathVariable("roleId") String roleId) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        long countCheck = sysRolesAuthoritiesService.countByRoleId(roleId);

        if(countCheck > 0){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg(" role has " + countCheck + " authorities, please delete authority under role first !");
            return responseResult;
        }

        sysRolesRepository.delete(roleId);

        return responseResult;
    }

    @RequestMapping(path = "/andauthorities/{roleId}/{authorityId}", method = RequestMethod.DELETE)
    public ResponseResult<Void> delete(@PathVariable("authorityId") String authorityId, @PathVariable("roleId") String roleId) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        sysRolesAuthoritiesService.deleteByAuthorityIdAndRoleId(authorityId, roleId);

        return responseResult;
    }

    @GetMapping(path = "/{roleId}")
    public ResponseResult<SysRolesEntity> getSysAuthoritiesEntity(@PathVariable("roleId") String roleId) {
        ResponseResult<SysRolesEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");

        SysRolesEntity sysRolesEntity = sysRolesRepository.findOne(roleId);

        if(sysRolesEntity == null){
            sysRolesEntity = sysRolesRepository.findByRoleName(roleId);
        }

        if(sysRolesEntity == null){
            responseResult.setResponseCode("10000");
            responseResult.setResponseMsg("could not find role entity by roleId or roleName");
            return responseResult;
        }

        responseResult.setData(sysRolesEntity);

        return responseResult;
    }

    @RequestMapping(path = "/andauthorities/{roleId}", method = RequestMethod.GET)
    public ResponseResult<RoleAndAuthorities> getSysAuthoritiesAndResources(@PathVariable("roleId") String roleId,RoleAuthorityRequestParams rarp, HttpServletRequest request) {
        ResponseResult<RoleAndAuthorities> responseResult = sysRolesAuthoritiesService.findRoleAndAuthorities(roleId, rarp);
        return responseResult;
    }

    @RequestMapping(path = "/andauthorities/{roleId}", method = RequestMethod.POST)
    public ResponseResult<Void> addSysAuthoritiesAndResources(@PathVariable("roleId") String roleId, @RequestBody List<SysAuthoritiesEntity> authorities, HttpServletRequest request) {
        ResponseResult<Void> responseResult = sysRolesAuthoritiesService.addAuthoritiesUnderRole(roleId, authorities);
        return responseResult;
    }

    @RequestMapping(path = "/andauthorities/not/{roleId}", method = RequestMethod.GET)
    public ResponseResult<Page<SysAuthoritiesEntity>> getResourcesNotUnderAuthority(@PathVariable("roleId") String roleId,RoleAuthorityRequestParams rarp, HttpServletRequest request) {

        Page<SysAuthoritiesEntity> resourcesEntities = sysRolesAuthoritiesService.findAuthoritiesNotUnderRole(roleId, rarp);
        ResponseResult<Page<SysAuthoritiesEntity>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setResponseMsg("");
        responseResult.setData(resourcesEntities);

        return responseResult;
    }
}
