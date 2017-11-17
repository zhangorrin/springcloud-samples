package com.orrin.sca.common.service.uaa.server.dao;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysRolesAuthoritiesEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Orrin on 2017/7/8.
 */
@Repository("sysRolesAuthoritiesRepository")
public interface SysRolesAuthoritiesRepository extends BaseJPARepository<SysRolesAuthoritiesEntity, String> {
    long countByRoleId(String roleId);

    SysRolesAuthoritiesEntity findByAuthorityIdAndRoleId(String authorityId, String roleId);

    void deleteByAuthorityIdAndRoleId(String authorityId, String roleId);

    @Query(value = " select distinct sae from SysRolesAuthoritiesEntity srae, SysAuthoritiesEntity sae " +
            " where srae.authorityId = sae.authorityId " +
            "   and sae.enable = true " +
            "   and srae.roleId = :roleId " +
            "   and sae.authorityMark like CONCAT('%', :authorityMark ,'%') " +
            "   and sae.authorityName like CONCAT('%', :authorityName ,'%')  ")
    Page<SysAuthoritiesEntity> findAuthoritiesUnderRole(@Param("roleId") String roleId, @Param("authorityMark") String authorityMark, @Param("authorityName") String authorityName, Pageable pageable);

    @Query(value = " select distinct sae from SysAuthoritiesEntity sae " +
            " where sae.enable = true " +
            "   and sae.authorityMark like CONCAT('%', :authorityMark ,'%') " +
            "   and sae.authorityName like CONCAT('%', :authorityName ,'%')  " +
            "and not exists (" +
            "    select 1 from  SysRolesAuthoritiesEntity srae " +
            "    where srae.authorityId = sae.authorityId" +
            "      and srae.roleId = :roleId " +
            "    ) "  )
    Page<SysAuthoritiesEntity> findAuthoritiesNotUnderRole(@Param("roleId") String roleId, @Param("authorityMark") String authorityMark, @Param("authorityName") String authorityName, Pageable pageable);


}
