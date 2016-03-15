package i_start;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class StartTest {
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	 
	/**�������̶���inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("start.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("start.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("��ʼ�")
				.addInputStream("start.bpmn", inBpmn)
				.addInputStream("start.png", inPng)
				.deploy();
		System.out.println("����Id��"+dp.getId());
		System.out.println("�������ƣ�"+dp.getName());
		//����Id��90001 �������ƣ���������
	}
	
	/*
	 * ��������ʵ��
	 * 1)�����ݿ��act_ru_execution����ִ�е�ִ�ж�����в���һ����¼
    2)�����ݿ��act_hi_procinst��ʵ������ʷ���в���һ����¼
    3)�����ݿ��act_hi_actinst��ڵ����ʷ���в���һ����¼
    4)����ͼ�нڵ㶼������ڵ㣬����ͬʱҲ����act_ru_task����ʵ������ʷ�����һ����¼
    5)�����ݿ��act_hi_taskinst������ʷ����Ҳ����һ����¼��
	 * */
	@Test 
	public void statProcessInstance(){
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("startProcess")// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// ʹ�����̶����key��������ʵ����key��ӦHelloWorld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID��"+pi.getId());//����ʵ��ID 92501 ����ʵ��ID��exclusiveGateWay:1:90004
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId());
		/***�ж������Ƿ��������ѯ����ִ�е�ִ�ж����*/
		ProcessInstance rpi=processEngine.getRuntimeService()
			.createProcessInstanceQuery()
			.processInstanceId(pi.getId())
			.singleResult();
		//˵������ʵ��������
		if(rpi==null){
			//��ѯ��ʷ����ȡ���������Ϣ
			HistoricProcessInstance hpi=processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId(pi.getId())
				.singleResult();
			System.out.println(hpi.getId());
		}
	}
	
}
