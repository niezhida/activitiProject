import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class ProcessInstanceTest {
	//����ʵ��
	
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**�������̶���zip*/
	@Test
	public void deploymentProcessDefinition_zip(){
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/hellowordWF.zip");
		ZipInputStream zis=new ZipInputStream(in);
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("")
				.addZipInputStream(zis)
				.deploy();
		System.out.println("����Id��"+dp.getId());
		System.out.println("�������ƣ�"+dp.getName());
		//20001
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
		//RuntimeService rts=pes.getRuntimeService();//����ִ�е�����ʵ����ִ�ж�����ص�Service
		//ʹ�����̶����key��������ʵ����key��ӦhelloWorld.bpmn �ļ���id������ֵ,ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		//ProcessInstance pinst=rts.startProcessInstanceByKey("helloworld");
		
		//System.out.println("����ʵ��ID��"+pinst.getId());//����ʵ��ID 2501
		//System.out.println("���̶���ID:"+pinst.getProcessDefinitionId());//���̶���id  helloworld:1:4
		
		ProcessInstance pi = processEngine.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
	            .startProcessInstanceByKey("helloworld");// ʹ�����̶����key��������ʵ����key��ӦHelloWorld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID��"+pi.getId());//����ʵ��ID 2501 ����ʵ��ID��22501
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
		TaskQuery tqA=tq.taskAssignee("����")//����ָ�����������ѯ��ָ��������
				//.taskCandidateUser(candidateUser)//������Ĵ����˲�ѯ
				//.processDefinitionId("")//���̶���id��ѯ
				//.processInstanceId(processInstanceId)//����ʵ��id��ѯ
				//.executionId(executionId)//ִ�ж���id��ѯ
				//����
				//orderByTaskCreateTime().asc()//����ʱ������
				//.singleResult()//����λ�ƽ����
				//.count();
				;
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
		String taskId="22504";
		processEngine.getTaskService().complete(taskId);;
		System.out.println("�������");
	}
	
	/**��ѯ����״̬���ж���������ִ�л��ǽ���2333*/
	@Test
	public void isProcessEnd(){
		String processInstanceId="";
		ProcessInstance pl=processEngine.getRuntimeService()//��ʾ����ִ�е�����ʵ����ִ�ж���
				.createProcessInstanceQuery()//��������ʵ����ѯ
				.processInstanceId("111")//ʹ������ʵ��id��ѯ
				.singleResult();
		if(pl==null){
			System.out.println("����");
		}else{
			System.out.println("û�н���");
		}
		//System.out.println(pl.isEnded());
	}
	
	/**��ѯ��ʷ����*/
	@Test
	public void findHistoryTask(){
		List<HistoricTaskInstance> l=processEngine.getHistoryService()
			.createHistoricTaskInstanceQuery()//������ʷ����ʵ����ѯ
			.taskAssignee("����")//��ʷ���������
			.list();
		if(l!=null&&l.size()>0){
			for(HistoricTaskInstance h:l){
				System.out.println(h.getId()+","+h.getName()+","+h.getProcessInstanceId()+","+h.getStartTime()+","+h.getEndTime()+",����:"+h.getDurationInMillis());
			}
		}
	}
	
	/**��ѯ��ʷ����ʵ��23333*/
	@Test
	public void findHistoryProcessInstance(){
		HistoricProcessInstance h=processEngine.getHistoryService()
			.createHistoricProcessInstanceQuery()//��ʷ����ʵ��
			.processInstanceId("27501")//����ʵ��id
			.singleResult();//
		System.out.println(h.getId()+","+h.getProcessDefinitionId()+","+h.getStartTime()+","+h.getEndTime()+",����:"+h.getDurationInMillis());
	}
}
