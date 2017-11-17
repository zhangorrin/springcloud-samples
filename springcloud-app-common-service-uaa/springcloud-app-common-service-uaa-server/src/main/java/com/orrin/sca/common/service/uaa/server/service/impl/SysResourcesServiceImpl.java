package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.apigateway.client.domain.ZuulRouteEntity;
import com.orrin.sca.common.service.apigateway.client.feignclient.ZuulRouteServiceApi;
import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysResourcesService;
import com.orrin.sca.common.service.uaa.server.dao.OauthClientDetailsRepository;
import com.orrin.sca.common.service.uaa.server.dao.SysResourcesRepository;
import com.orrin.sca.component.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("sysResourcesService")
public class SysResourcesServiceImpl implements SysResourcesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysResourcesServiceImpl.class);

	@Autowired
	private SysResourcesRepository sysResourcesRepository;

	@Autowired
	private OauthClientDetailsRepository oauthClientDetailsRepository;

	@Autowired
	private ZuulRouteServiceApi zuulRouteServiceApi;

	@Override
	public Page<SysResourcesEntity> findNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size);
		return sysResourcesRepository.findAll(pageable);
	}

	@Override
	public List<SysResourcesEntity> findAllMenuSysResources() {
		return sysResourcesRepository.findByResourceTypeAndEnableOrderByPriorityAscZuulRouteIdAsc("MENU", true);
	}

	@Override
	public List<MenuModel> wrapMenu(List<SysResourcesEntity> sysResourcesEntityList) {
		List<MenuModel> menus = new ArrayList<>();
		List<ZuulRouteEntity> clientList = zuulRouteServiceApi.getAllZuulRoute().getData();
		Map<String,List<SysResourcesEntity>> decomposeResourceOnClientIdMap = new HashMap<>();
		if(clientList != null && clientList.size() > 0) {

			for (SysResourcesEntity sre : sysResourcesEntityList) {
				if (decomposeResourceOnClientIdMap.containsKey(sre.getZuulRouteId())) {
					decomposeResourceOnClientIdMap.get(sre.getZuulRouteId()).add(sre);
				} else {
					List<SysResourcesEntity> a = new ArrayList<SysResourcesEntity>();
					a.add(sre);
					decomposeResourceOnClientIdMap.put(sre.getZuulRouteId(), a);
				}
			}

			for (ZuulRouteEntity zuulRoute : clientList) {
				MenuModel menuModel = new MenuModel();
				menuModel.setResourceId(zuulRoute.getServiceId());
				menuModel.setResourceName(zuulRoute.getServiceName());

				menuModel.setTitle(zuulRoute.getServiceName());
				menuModel.setChildren(this.wrapMenu(zuulRoute.getZuulRouteId(), decomposeResourceOnClientIdMap.get(zuulRoute.getZuulRouteId())));
				menus.add(menuModel);
			}
		}

		return menus;
	}

	@Override
	@Cacheable(value = "common-service-uaa:menuModels", key = "'system-menus'")
	public List<MenuModel> wrapMenu() {
		LOGGER.info("wrapMenu");
		List<SysResourcesEntity> resourcesEntityList = this.findAllMenuSysResources();
		List<MenuModel> menuModels = this.wrapMenu(resourcesEntityList);
		return menuModels;
	}

	@Override
	@CacheEvict(value = "common-service-uaa:menuModels", key = "'system-menus'")
	@Transactional(rollbackFor = Exception.class)
	public SysResourcesEntity saveAndFlush(SysResourcesEntity sysResourcesEntity) {
		return sysResourcesRepository.saveAndFlush(sysResourcesEntity);
	}

	@Override
	@CacheEvict(value = "common-service-uaa:menuModels", key = "'system-menus'")
	@Transactional(rollbackFor = Exception.class)
	public void delete(String resourceId) {
		sysResourcesRepository.delete(resourceId);
	}

	protected List<MenuModel> wrapMenu(String clientId, List<SysResourcesEntity> sysResourcesEntityList) {
		List<MenuModel> menus = new ArrayList<>();
		if(sysResourcesEntityList != null){
			for(SysResourcesEntity sre : sysResourcesEntityList) {
				if(StringUtils.hasText(sre.getFatherResourceId()) && sre.getFatherResourceId().equals(clientId)){
					MenuModel menuModel = SysResourcesEntity.fromSysResources(sre);
					menuModel.setChildren(this.findMenuChildren(sre.getResourceId(),sysResourcesEntityList));
					menus.add(menuModel);
				}
			}
		}

		return menus;
	}


	protected List<MenuModel> findMenuChildren(String resourceId,List<SysResourcesEntity> sysResourcesEntityList) {
		List<MenuModel> menus = new ArrayList<>();
		for(SysResourcesEntity sre : sysResourcesEntityList) {
			if(resourceId.equals(sre.getFatherResourceId())){
				MenuModel menuModel = SysResourcesEntity.fromSysResources(sre);
				menuModel.setChildren(this.findMenuChildren(sre.getResourceId(),sysResourcesEntityList));
				menus.add(menuModel);
			}
		}

		return menus;
	}

	/*@Override
	public Page<SysResources> findCriteria(Integer page, Integer size, SysResources sysResources) {
		return null;
	}*/
}
