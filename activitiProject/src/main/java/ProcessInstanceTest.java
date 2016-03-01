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
	//流程实例
	
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义zip*/
	@Test
	public void deploymentProcessDefinition_zip(){
		InputStream in =this.getClass().getClassLoader().getResourceAsStream("diagrams/hellowordWF.zip");
		ZipInputStream zis=new ZipInputStream(in);
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("")
				.addZipInputStream(zis)
				.deploy();
		System.out.println("部署Id："+dp.getId());
		System.out.println("部署名称："+dp.getName());
		//20001
	}
	
	/*
	 * 启动流程实例
	 * 1)在数据库的act_ru_execution正在执行的执行对象表中插入一条记录
    2)在数据库的act_hi_procinst程实例的历史表中插入一条记录
    3)在数据库的act_hi_actinst活动节点的历史表中插入一条记录
    4)我们图中节点都是任务节点，所以同时也会在act_ru_task流程实例的历史表添加一条记录
    5)在数据库的act_hi_taskinst任务历史表中也插入一条记录。
	 * */
	@Test 
	public void statProcessInstance(){
		//RuntimeService rts=pes.getRuntimeService();//正在执行的流程实例和执行对象相关的Service
		//使用流程定义的key启动流程实例，key对应helloWorld.bpmn 文件中id的属性值,使用key值启动，默认是按照最新版本的流程定义启动
		//ProcessInstance pinst=rts.startProcessInstanceByKey("helloworld");
		
		//System.out.println("流程实例ID："+pinst.getId());//流程实例ID 2501
		//System.out.println("流程定义ID:"+pinst.getProcessDefinitionId());//流程定义id  helloworld:1:4
		
		ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service  
	            .startProcessInstanceByKey("helloworld");// 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID："+pi.getId());//流程实例ID 2501 流程实例ID：22501
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
	}
	
	/**
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonaltask(){
		TaskService ts=processEngine.getTaskService();//正在执行的任务管理相关的Service
		TaskQuery tq=ts.createTaskQuery();//创建任务查询对象
		//查询条件
		TaskQuery tqA=tq.taskAssignee("张三")//张三指定个人任务查询，指定办理人
				//.taskCandidateUser(candidateUser)//组任务的代办人查询
				//.processDefinitionId("")//流程定义id查询
				//.processInstanceId(processInstanceId)//流程实例id查询
				//.executionId(executionId)//执行对象id查询
				//排序
				//orderByTaskCreateTime().asc()//创建时间升序
				//.singleResult()//返回位移结果集
				//.count();
				;
		List<Task> list=tqA.list();
		System.out.println(list);
		if(list!=null&&list.size()>0){
			for(Task t:list){
				System.out.println("任务id:"+t.getId());
				System.out.println("任务名称:"+t.getName());
				System.out.println("任务的创建时间:"+t.getCreateTime());
				System.out.println("任务办理人:"+t.getAssignee());
				System.out.println("流程实例Id:"+t.getProcessInstanceId());
				System.out.println("执行对象Id:"+t.getExecutionId());
				System.out.println("流程定义Id:"+t.getProcessDefinitionId());
			}
			
		}
	}
	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask(){
		String taskId="22504";
		processEngine.getTaskService().complete(taskId);;
		System.out.println("完成任务");
	}
	
	/**查询流程状态，判断流程正在执行还是结束2333*/
	@Test
	public void isProcessEnd(){
		String processInstanceId="";
		ProcessInstance pl=processEngine.getRuntimeService()//表示正在执行的流程实例合执行对象
				.createProcessInstanceQuery()//创建流程实例查询
				.processInstanceId("111")//使用流程实例id查询
				.singleResult();
		if(pl==null){
			System.out.println("结束");
		}else{
			System.out.println("没有结束");
		}
		//System.out.println(pl.isEnded());
	}
	
	/**查询历史任务，*/
	@Test
	public void findHistoryTask(){
		List<HistoricTaskInstance> l=processEngine.getHistoryService()
			.createHistoricTaskInstanceQuery()//创建历史任务实例查询
			.taskAssignee("张三")//历史人物办理人
			.list();
		if(l!=null&&l.size()>0){
			for(HistoricTaskInstance h:l){
				System.out.println(h.getId()+","+h.getName()+","+h.getProcessInstanceId()+","+h.getStartTime()+","+h.getEndTime()+",毫秒:"+h.getDurationInMillis());
			}
		}
	}
	
	/**查询历史流程实例23333*/
	@Test
	public void findHistoryProcessInstance(){
		HistoricProcessInstance h=processEngine.getHistoryService()
			.createHistoricProcessInstanceQuery()//历史流程实例
			.processInstanceId("27501")//流程实例id
			.singleResult();//
		System.out.println(h.getId()+","+h.getProcessDefinitionId()+","+h.getStartTime()+","+h.getEndTime()+",毫秒:"+h.getDurationInMillis());
	}
}
