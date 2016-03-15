package j_receiveTask;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskTest {
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	 
	/**�������̶���inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("receiveTask.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("���ջ����")
				.addInputStream("receiveTask.bpmn", inBpmn)
				.addInputStream("receiveTask.png", inPng)
				.deploy();
		System.out.println("����Id��"+dp.getId());
		System.out.println("�������ƣ�"+dp.getName());
		//����Id��90001 �������ƣ���������
	}
	
	@Test 
	public void statProcessInstance(){
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("receiveTask")// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// ʹ�����̶����key��������ʵ����key��ӦHelloWorld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID��"+pi.getId());//����ʵ��ID 92501 ����ʵ��ID��exclusiveGateWay:1:90004
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId());
		//��ѯִ�ж���id
		Execution e=processEngine.getRuntimeService().createExecutionQuery()
			.processInstanceId(pi.getId())
			.activityId("receivetask1").singleResult();//��ǰ���id��  ��Ӧreceivetask.bpmn�ļ��е�idֵ
			
		//ʹ�����̱������õ������۶��������ҵ�����
		processEngine.getRuntimeService().setVariable(e.getId(), "���ܵ������۶�", 21000);
		
		//���ִ���첽��������̴��ڵȴ�״̬��ʹ�����̼���ִ��
		processEngine.getRuntimeService().signal(e.getId());
		
		//��ѯִ�ж���id
		Execution e2=processEngine.getRuntimeService().createExecutionQuery()
				.processInstanceId(pi.getId())
				.activityId("receivetask2").singleResult();
		//�����̱����л�ȡ���ܵ������۶��ֵ
		Integer value=(Integer) processEngine.getRuntimeService().getVariable(e2.getId(), "���ܵ������۶�");
		System.out.println("�����ŷ��Ͷ��ţ�����ǣ�"+value);
		
		//���ִ���첽��������̴��ڵȴ�״̬��ʹ�����̼���ִ��
		processEngine.getRuntimeService().signal(e2.getId());
	}
}
