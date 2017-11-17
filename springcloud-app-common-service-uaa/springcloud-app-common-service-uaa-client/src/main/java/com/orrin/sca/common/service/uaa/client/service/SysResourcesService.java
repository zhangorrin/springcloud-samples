package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.component.menu.MenuModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysResourcesService {
	Page<SysResourcesEntity> findNoCriteria(Integer page, Integer size);
	//Page<SysResourcesEntity> findCriteria(Integer page, Integer size, SysResourcesEntity sysResources);

	List<SysResourcesEntity> findAllMenuSysResources();

	List<MenuModel> wrapMenu(List<SysResourcesEntity> sysResourcesEntityList);

	List<MenuModel> wrapMenu();

	SysResourcesEntity saveAndFlush(SysResourcesEntity sysResourcesEntity);

	void delete(String resourceId);
}
