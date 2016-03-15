package g_exclusiveGateWay;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class ExclusiveGateWayTest {
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	 
	/**�������̶���inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("exclusiveGateWay1.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("exclusiveGateWay1.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("��������")
				.addInputStream("exclusiveGateWay.bpmn", inBpmn)
				.addInputStream("exclusiveGateWay.png", inPng)
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
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("exclusiveGateWay")// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// ʹ�����̶����key��������ʵ����key��ӦHelloWorld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID��"+pi.getId());//����ʵ��ID 92501 ����ʵ��ID��exclusiveGateWay:1:90004
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId());
	}
	
	/**
	 * ��ѯ��ǰ�˵ĸ�������
	 */
	@Test
	public void findMyPersonaltask(){
		TaskService ts=processEngine.getTaskService();//����ִ�е����������ص�Service
		TaskQuery tq=ts.createTaskQuery();//���������ѯ����
		//��ѯ����
		//TaskQuery tqA=tq.taskUnassigned()//����ָ�����������ѯ��ָ��������,������Ϊ�յ�ʱ��δ��ְ���
				//.taskUnnassigned();
		TaskQuery tqA=tq.taskAssignee("���ž�������");//���ñ�������,���ž�������
		//TaskQuery tqA=tq.taskAssignee("����");
		List<Task> list=tqA.list();
		System.out.println(list);
		if(list!=null&&list.size()>0){
			for(Task t:list){
				System.out.println("����id:"+t.getId());
				System.out.println("��������:"+t.getName());
				System.out.println("����Ĵ���ʱ��:"+t.getCreateTime());
				System.out.println("���������:"+t.getAssignee());
				System.out.println("����ʵ��Id:"+t.getProcessInstanceId());
				System.out.println("ִ�ж���Id:"+t.getExecutionId());
				System.out.println("���̶���Id:"+t.getProcessDefinitionId());
			}
			
		}
	}
	
	/**����ҵ�����*/
	@Test
	public void completeMyPersonalTask(){
		String taskId="125004";
		Map v=new HashMap();
		//v.put("message", "����Ҫ");
		//v.put("message", "��Ҫ");
		v.put("money", 800);
		processEngine.getTaskService().complete(taskId,v);;
		System.out.println("�������");
	}
}
