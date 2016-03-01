
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessVariablesTest {
	
	@Test
	public void test(/*String[] args*/) {//initializationError runner:junit4
		//��classpath��Ŀ¼�¼���ָ�����Ƶ��ļ�
		this.getClass().getClassLoader()
			.getResourceAsStream("testVariables.bpmn");
		
		//�ӵ�ǰ���¼���ָ�����Ƶ��ļ�
		this.getClass().getResourceAsStream("testVariables.bpmn");
		
		//��calsspath��Ŀ¼�¼���ָ�����Ƶ��ļ�
		this.getClass().getResourceAsStream("/testVariables.bpmn");
		
	}
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**�������̶���(InputStream)*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inputStreambpmn=this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		
		InputStream inputStreampng=this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		Deployment deployment=processEngine.getRepositoryService()
				.createDeployment()
				.name("���̶���1")
				.addInputStream("processVariables.bpmn", inputStreambpmn)//Ҫ������Դ�ļ�������Ҫһ��
				.addInputStream("processVariables.png", inputStreampng)//Ҫ������Դ�ļ�������Ҫһ��
				.deploy();
		System.out.println("Id "+deployment.getId());
		System.out.println("���� "+deployment.getName());
		/*Id 30001
		���� ���̶���1*/
	}
	
	/**��������ʵ��*/
	@Test
	public void startProcessInstance(){
		//���̶���key
		String processDefinitionKey="processVariables";
		ProcessInstance pi=processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("ʵ��Id "+pi.getId());
		System.out.println("����Id "+pi.getProcessDefinitionId());
		/*ʵ��Id 32501
		����Id processVariables:1:30004*/
	}
	
	/**�������̱���*/
	@Test
	public void setVariables(){
		TaskService ts=processEngine.getTaskService();
		//ts.setVariableLocal("", "�������", 3);//Local �ֲ�����������id��
		//String taskId="32504";
		String taskId="40002";
		ts.setVariable(taskId, "�������-", 4);//
		ts.setVariable(taskId, "���ԭ��-", "�ؼ�̽��");
		//ts.setVariableLocal(taskId, "�������", new Date());
		//javabean����
		//��һ��javabeanʵ�����л������������̱����У�Ҫ��javabean�����Բ����ٷ����仯����������仯����ȡ���̱��������ʱ���׳��쳣
		//�����������Persion��������� �汾serialVersionUID
		
		Person p=new Person();
		p.setId(1);
		p.setName("persion1111����");
		//ts.setVariable(taskId,"java����",p);
		//ts.setVariable(taskId,"java����汾1",p);
		//ts.setVariable(taskId,"java����汾2",p);
		
		p.setTest("fff");
		ts.setVariable(taskId,"java����汾3",p);
		System.out.println("���óɹ�");
	}
	
	/**��ȡ���̱���*/
	@Test
	public void getVariables(){
		TaskService ts=processEngine.getTaskService();
		//String tId="32504";
		String tId="40002";
		Integer l=(Integer) ts.getVariable(tId, "�������");
		Date d=(Date) ts.getVariable(tId, "�������");
		String str=(String) ts.getVariable(tId, "���ԭ��");
		System.out.println(l);
		System.out.println(d);
		System.out.println(str);
		
		//Person p=(Person) ts.getVariable(tId, "java����");
		//Person p=(Person) ts.getVariable(tId, "java����汾1");
		//Person p=(Person) ts.getVariable(tId, "java����汾2");
		Person p=(Person) ts.getVariable(tId, "java����汾3");
		System.out.println(p.toString());
	}
	
	/**ģ�����úͻ�ȡ���̱����ĳ���*/
	@Test
	public void setAndGetVariables(){
		/**������ʵ����ִ�ж������*/
		RuntimeService rs=processEngine.getRuntimeService();
		//��������� ������ִ�У�
		TaskService taskService=processEngine.getTaskService();
		
		//�������̱���
		//��ʾʹ��ִ�ж���id�������̱��������ƣ��������̱�����ֵ��һ��ֻ������һ��ֵ
		//rs.setVariable(executionId, variableName, value);;
		//ʹ��ִ�ж���id��Map���ϣ�map�е�keyֵ�������̱���������,���ö������
		//rs.setVariables(executionId, variables);
		
		//
		//taskService.setVariable(taskId, variableName, value);
		//taskService.setVariables(taskId, variables);
		
		//��������ʵ����ͬ�£��������̱��� Map
		//rs.startProcessInstanceByKey(processDefinitionKey, variables)
		
		//��������ͬ���������̱���
		//taskService.complete(taskId, variables);
		
		//rs.getVariable(executionId, variableName);//ִ�ж���id+���̱������ƣ���ȡ���̱�����ֵ
		//rs.getVariables(executionId)//��ȡ�������̱���������map
		//rs.getVariables(executionId, variableNames)//ִ�ж���id����ȡ���̱������ϣ�ͨ���������̱��������Ƽ��ϴ�ŵ�map��
		
		//taskService.getVariable(taskId, variableName);//����id�����̱�������
		//taskService.getVariables(taskId);//��ȡ����
		//taskService.getVariables("",variableNames);//����id ��ȡ���̱������ϣ�ͨ���������̱��������Ƽ��ϴ�ŵ�map��
	}
	
	/**����ҵ�����*/
	@Test
	public void completeMyPersonalTask(){
		String taskid="32504";
		processEngine.getTaskService().complete(taskid);
		System.out.println("���  ����id��"+taskid);
	}
	
	/**��ѯ���̱�������ʷ��*/
	@Test
	public void findHistoryProcessVariables(){
		List<HistoricVariableInstance> list=processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery()
				.variableName("�������")
				.list();
		if(list!=null&&list.size()>0){
			for(HistoricVariableInstance h:list){
				System.out.println(h.getId()+","+h.getProcessInstanceId()+","+h.getVariableName());
			}
		}
	}
}
