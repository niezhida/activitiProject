import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
public class HelloWorld {
	ProcessEngine pes=ProcessEngines.getDefaultProcessEngine();
	
	/*
	 * �������̶���
	 * select * from act_re_procdef;
	 * */
	@Test
	public void deploymentProcessDefinition(){
		//���bug����helloworld.bpmn�ر� activiti.cfg.xml
		System.out.println(pes);
		RepositoryService rps=pes.getRepositoryService();//�����̶���Ͳ��������ص�Service
		DeploymentBuilder dlmb=rps.createDeployment();//����һ���������
		dlmb.name("helloWorld���ų���");//��Ӳ��������
		dlmb.addClasspathResource("diagrams/helloworld1.bpmn");//��classpath����Դ�м��أ�һ��ֻ�ܼ���һ���ļ�
		dlmb.addClasspathResource("diagrams/helloworld1.png");
		Deployment dpl=dlmb.deploy();//��ɲ���
		System.out.println("����ID��"+dpl.getId());
		System.out.println("��������:"+dpl.getName());
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
		
		ProcessInstance pi = pes.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص�Service  
	            .startProcessInstanceByKey("helloworld");// ʹ�����̶����key��������ʵ����key��ӦHelloWorld.bpmn�ļ���id������ֵ��ʹ��keyֵ������Ĭ���ǰ������°汾�����̶�������
		System.out.println("����ʵ��ID��"+pi.getId());//����ʵ��ID 2501
		System.out.println("���̶���ID:"+pi.getProcessDefinitionId());
	}
	/** 
	 * ��ѯ��ʷ����ʵ�� 
	 */  
	@Test  
	public void findHistoryProcessInstance(){  
	    String processInstanceId="7501";  
	    HistoricProcessInstance hpi = pes.getHistoryService()  
	            .createHistoricProcessInstanceQuery()  
	            .processInstanceId(processInstanceId)  
	            .singleResult();  
	    System.out.println(hpi.getId() +"    "+hpi.getProcessDefinitionId()+"   "+ hpi.getStartTime()+"   "+hpi.getDurationInMillis());  
	}
	
	/**
	 * ��ѯ��ǰ�˵ĸ�������
	 */
	@Test
	public void findMyPersonaltask(){
		TaskService ts=pes.getTaskService();//����ִ�е����������ص�Service
		TaskQuery tq=ts.createTaskQuery();//���������ѯ����
		TaskQuery tqA=tq.taskAssignee("����");//����ָ�����������ѯ��ָ��������
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
		 /*String assignee = "����"; // TODO  
		    List<Task> list = pes.getTaskService()// ������ִ�е����������ص�service  
		            .createTaskQuery()// ���������ѯ����  
		            // ��ѯ����  
		            .taskAssignee(assignee)// ָ�����������ѯ��ָ��������  
		            // .taskCandidateGroup("")//������İ����˲�ѯ  
		            // .processDefinitionId("")//ʹ�����̶���ID��ѯ  
		            // .processInstanceId("")//ʹ������ʵ��ID��ѯ  
		            // .executionId(executionId)//ʹ��ִ�ж���ID��ѯ  
		            *//** ���� *//*  
		            .orderByTaskCreateTime().asc()// ʹ�ô���ʱ�����������  
		            // ���ؽ����  
		            // .singleResult() //����Ψһ�Ľ����  
		            // .count()//���ؽ����������  
		            // .listPage(firstResult, maxResults)//��ҳ��ѯ  
		            .list();// �����б�  
		    if (list != null && list.size() > 0) {  
		        for (Task task : list) {  
		            System.out.println("����ID��" + task.getId());  
		            System.out.println("��������:" + task.getName());  
		            System.out.println("����Ĵ���ʱ��:" + task.getCreateTime());  
		            System.out.println("����İ�����:" + task.getAssignee());  
		            System.out.println("����ʵ��ID:" + task.getProcessInstanceId());  
		            System.out.println("ִ�ж���ID:" + task.getExecutionId());  
		            System.out.println("���̶���ID:" + task.getProcessDefinitionId());  
		            System.out  
		                    .println("##################################################");  
		        }
		    }*/
	}
	
	/**
	 * ����ҵ�����
	 *
	 **/
	@Test 
	public void completeMyPersonalTask(){
		//����id
		String taskId="7504";
		TaskService ts=pes.getTaskService();//ִ�����������ص�service
		ts.complete(taskId);
		System.out.println("�����������id:"+taskId);
	}
	
	/** 
	 * ��ѯ��ʷ���� 
	 */  
	@Test  
	public void findHistoryTask(){  
	    String processInstanceId="7501";  
	    List<HistoricTaskInstance> list = pes.getHistoryService()//����ʷ���ݣ���ʷ����ص�service  
	            .createHistoricTaskInstanceQuery()//������ʷ����ʵ����ѯ  
	            .processInstanceId(processInstanceId)  
//	              .taskAssignee(taskAssignee)//ָ����ʷ����İ�����  
	            .list();  
	    if(list!=null && list.size()>0){  
	        for(HistoricTaskInstance hti:list){  
	            System.out.println(hti.getId()+"    "+hti.getName()+"    "+hti.getProcessInstanceId()+"   "+hti.getStartTime()+"   "+hti.getEndTime()+"   "+hti.getDurationInMillis());  
	            System.out.println("################################");  
	        }  
	    }     
	  
	}
	
	/** 
	 * ��ѯ����״̬���ж���������ִ�У����ǽ����� 
	 */  
	@Test  
	public void isProcessEnd(){  
	    String processInstanceId =  "7501";  
	    ProcessInstance pi = pes.getRuntimeService()//��ʾ����ִ�е�����ʵ����ִ�ж���  
	            .createProcessInstanceQuery()//��������ʵ����ѯ  
	            .processInstanceId(processInstanceId)//ʹ������ʵ��ID��ѯ  
	            .singleResult();  
	      
	    if(pi==null){  
	        System.out.println("�����Ѿ�����");  
	    }  
	    else{  
	        System.out.println("����û�н���");  
	    }  
	      
	} 
}
