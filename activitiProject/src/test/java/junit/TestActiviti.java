package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActiviti {
	/**
	 * ʹ�ô��봴����������Ҫ23�ű�
	 */
	@Test
	public void createTable(){
		ProcessEngineConfiguration pec//processEngineConfiguration
			=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		//�������ݿ������
		pec.setJdbcDriver("com.mysql.jdbc.Driver");
		pec.setJdbcUrl("jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=utf8");
		pec.setJdbcUsername("root");
		pec.setJdbcPassword("123456");
		
		/**
		  public static final String DB_SCHEMA_UPDATE_FALSE = "false";�����Զ���������Ҫ�����
  
		   Creates the schema when the process engine is being created and 
		   * drops the schema when the process engine is being closed. 
		   * ��ɾ�����ڴ�����
		  public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
		
		   Upon building of the process engine, a check is performed and 
		   * an update of the schema is performed if it is necessary. 
		   * ��������ڣ��Զ�������
		  public static final String DB_SCHEMA_UPDATE_TRUE = "true";
		 * 
		 */
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		//�������ĺ��Ķ���ProcessEnginee����
		ProcessEngine pe=pec.buildProcessEngine();
		System.out.println("processEngine:"+pe);
		
	}
}
