package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Test;

public class TestActiviti {
	/**
	 * 使用代码创建工作流需要23张表
	 * mysql-connector-java-5.1.9.jar
	 * mybatis-3.2.3.jar
	 * slf4j-api-1.6.2.jar
	 * 
	 * jackson-databind-2.2.3.jar
	 * jackson-core-2.2.3.jar
	 * jackson-annotations-2.2.3.jar
	 * 
	 * joda-time-2.9.1.jar
	 * commons-lang3-3.1.jar
	 * 
	 * https://github.com/
	 * https://github.com/niezhida/activitiProject.git
	 * 
	 */
	@Test
	public void createTable(){
		System.out.println("createTable");
		ProcessEngineConfiguration pec//processEngineConfiguration
			=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		//连接数据库的配置
		pec.setJdbcDriver("com.mysql.jdbc.Driver");
		pec.setJdbcUrl("jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=utf8");
		pec.setJdbcUsername("root");
		pec.setJdbcPassword("123456");
		
		/**
		  public static final String DB_SCHEMA_UPDATE_FALSE = "false";不能自动创建表，需要表存在
  
		   Creates the schema when the process engine is being created and 
		   * drops the schema when the process engine is being closed. 
		   * 先删除表在创建表
		  public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
		
		   Upon building of the process engine, a check is performed and 
		   * an update of the schema is performed if it is necessary. 
		   * 如果表不存在，自动创建表
		  public static final String DB_SCHEMA_UPDATE_TRUE = "true";
		 * 
		 */
		//pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
		
		//工作流的核心对象，ProcessEnginee对象
		ProcessEngine pe=pec.buildProcessEngine();
		System.out.println("processEngine:"+pe);
		
	}
	
	//使用配置文件 
	/**
	 * spring-beans-4.1.6.RELEASE.jar
	 * spring-core-4.1.6.RELEASE.jar
	 * commons-logging-1.1.3.jar
	 * ;
	 * processEngineConfiguration
	 */
	@Test
	public void create2(){
		System.out.println("create2");
		/*ProcessEngineConfiguration p=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-custom-context.xml");
		ProcessEngine pe=p.buildProcessEngine();*/
		ProcessEngine pe=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
		System.out.println(pe);
		RepositoryService res=pe.getRepositoryService();//管理流程定义
		RuntimeService rus=pe.getRuntimeService();//执行管理，包括启动，推进，删除流程实例等操作
		TaskService ts=pe.getTaskService();//任务管理
		//HistoryService  历史管理(执行完的数据管理)
		//identityService 组织机构管理
		//FormService 一个可选服务，任务表单管理
		//ManagerService 
	}
}
