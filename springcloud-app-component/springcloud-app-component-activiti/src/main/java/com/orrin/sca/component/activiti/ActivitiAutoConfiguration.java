package com.orrin.sca.component.activiti;

import com.alibaba.druid.pool.DruidDataSource;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author orrin.zhang on 2017/8/14.
 * 详细可以参见https://www.activiti.org/userguide/index.html#springSpringBoot
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties(ActivitiProperties.class)
@ConditionalOnProperty(prefix = ActivitiProperties.PREFIX, value = "separateDB")
public class ActivitiAutoConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(ActivitiAutoConfiguration.class);

	@Autowired
	private DruidDataSource dataSource;

	@Autowired
	private ActivitiProperties activitiProperties;

	@Bean
	public CommandLineRunner init(final RepositoryService repositoryService,
								  final RuntimeService runtimeService,
								  final TaskService taskService) {

		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				logger.info("Number of process definitions : "
						+ repositoryService.createProcessDefinitionQuery().count());
				logger.info("Number of tasks : " + taskService.createTaskQuery().count());
				runtimeService.startProcessInstanceByKey("oneTaskProcess");
				logger.info("Number of tasks after process start: " + taskService.createTaskQuery().count());
			}
		};

	}

	/**
	 * Changing the database and connection pool
	 * @return
	 */
	@Bean
	public DataSource database() {
		if(activitiProperties.getSeparateDB()){
			return DataSourceBuilder.create()
					.url(activitiProperties.getDatabase().getUrl())
					.username(activitiProperties.getDatabase().getUsername())
					.password(activitiProperties.getDatabase().getPassword())
					.driverClassName(activitiProperties.getDatabase().getDriverClassName())
					.build();
		}
		return DataSourceBuilder.create()
				.url(dataSource.getUrl())
				.username(dataSource.getUsername())
				.password(dataSource.getPassword())
				.driverClassName(dataSource.getDriverClassName())
				.build();
	}


	/**
	 * 使用方法
	 */
	/*
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ManagementService managementService;
	*/

}
