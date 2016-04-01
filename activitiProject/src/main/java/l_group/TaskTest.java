package l_group;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.transformer.Identity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class TaskTest {
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	 
	/**�������̶���inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("task.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("task.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("����")
				.addInputStream("task.bpmn", inBpmn)
				.addInputStream("task.png", inPng)
				.deploy();
		System.out.println("����Id��"+dp.getId());
		System.out.println("�������ƣ�"+dp.getName());
		//����Id��90001 �������ƣ���������
	}
	
	@Test 
	public void statProcessInstance(){
		//ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("task");// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
		Map v=new HashMap();
		//v.put("userId", "������");
		v.put("userIds", "���,����,СС");
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("task",v)// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// ʹ�����̶����key��������ʵ����key��ӦHelloWorld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID��"+pi.getId());//����ʵ��ID 92501 ����ʵ��ID��exclusiveGateWay:1:90004
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId());
		//СA,СB,СC,СD
	}
	
	/**
	 * ��ѯ��ǰ�˵�������
	 */
	@Test
	public void findMyGrouptask(){
		TaskService ts=processEngine.getTaskService();//����ִ�е����������ص�Service
		TaskQuery tq=ts.createTaskQuery();//���������ѯ����
		//TaskQuery tqA=tq.taskCandidateUser("СA");
		//TaskQuery tqA=tq.taskCandidateUser("��H");
		TaskQuery tqA=tq.taskCandidateUser("���");
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
	
	/**
	 * ��ѯ��ǰ�˵ĸ�������
	 */
	@Test
	public void findMyPersonaltask(){
		TaskService ts=processEngine.getTaskService();//����ִ�е����������ص�Service
		TaskQuery tq=ts.createTaskQuery();//���������ѯ����
		TaskQuery tqA=tq.taskAssignee("��F");
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
		String taskId="172504";
		Map v=new HashMap();
		//v.put("message", "����Ҫ");
		//v.put("message", "��Ҫ");
		v.put("money", 800);
		processEngine.getTaskService().complete(taskId,v);;
		System.out.println("�������");
	}
	
	//(ת��) ���Է�����������һ���˵�����һ����(��������)
	@Test
	public void setAssigneeTask(){
		String taskId="182504";
		//ָ��������
		String userId="�Ŵ�ɽ";
		processEngine.getTaskService()
					.setAssignee(taskId, userId);;
	}
	
	//��ѯ����ִ�е���������,��ѡ������
	@Test
	public void findRunPersonTask(){
		String taskId="190004";
		List<IdentityLink> list=processEngine.getTaskService()
			.getIdentityLinksForTask(taskId);
		
		if(list!=null&&list.size()>0){
			for(IdentityLink t:list){
				System.out.println("����id:"+t.getTaskId());
				System.out.println("����:"+t.getType());
				System.out.println("����ʵ��id:"+t.getProcessInstanceId());
				System.out.println("���̶���id:"+t.getProcessDefinitionId());
				System.out.println("����ʵ��Id:"+t.getProcessInstanceId());
				System.out.println("u_Id:"+t.getUserId());
			}
			
		}
	}
	
	//��ѯ��ʷ��������  ������
	@Test
	public void findHistoryPersonTask(){
		String taskId="190004";
		List<HistoricIdentityLink> list=processEngine.getHistoryService()
			.getHistoricIdentityLinksForTask(taskId);
		
		if(list!=null&&list.size()>0){
			for(HistoricIdentityLink t:list){
				System.out.println("����id:"+t.getTaskId());
				System.out.println("����:"+t.getType());
				System.out.println("����ʵ��id:"+t.getProcessInstanceId());
				System.out.println("����ʵ��Id:"+t.getProcessInstanceId());
				System.out.println("u_Id:"+t.getUserId());
			}
			
		}
	}
	
	//ʰȡ���񣬽�������ָ���������(����ȡ)  ��ʰȡ��������Ͳ鲻��,(������תΪ��������)
	@Test
	public void claim(){
		String taskId="190004",userId="��F";
		//�����������(�������������еĳ�Ա��Ҳ�����Ƿ�������ĳ�Ա)
		processEngine.getTaskService().setAssignee(taskId, userId);
		
	}
	
	//������������˵�������(ǰ�ᣬ֮ǰһ����������)
	@Test
	public void setAssigee(){
		String taskId="190004";
		//�����������(�������������еĳ�Ա��Ҳ�����Ƿ�������ĳ�Ա)
		processEngine.getTaskService().setAssignee(taskId, null);
	} 
	
	//������������˵���������ӳ�Ա	
	@Test
	public void addGroupUser(){//���Ȼ����׷����ͬ�ģ������ͬ�Ĵ�H
		//����id
		String taskId="190004";
		processEngine.getTaskService()
			.addCandidateUser(taskId, "��H");
	}
	
	//��������ɾ����Ա ,����ɾ����ͬ����ĳ�Ա
	@Test
	public void deleteGroupUser(){
		//����id
		String taskId="190004";
		processEngine.getTaskService()
			.deleteCandidateUser(taskId, "��H");
	}
}

