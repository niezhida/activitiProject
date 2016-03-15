package k_personalTask02;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class TaskTest {
	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
	 
	/**部署流程定义inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("task.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("task.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("任务")
				.addInputStream("task.bpmn", inBpmn)
				.addInputStream("task.png", inPng)
				.deploy();
		System.out.println("部署Id："+dp.getId());
		System.out.println("部署名称："+dp.getName());
		//部署Id：90001 部署名称：排他网关
	}
	
	@Test 
	public void statProcessInstance(){
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("task")// 与正在执行的流程实例和执行对象相关的Service  
		//Map v=new HashMap();
		//v.put("userId", "周芷若");
		//ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("task",v)// 与正在执行的流程实例和执行对象相关的Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID："+pi.getId());//流程实例ID 92501 流程实例ID：exclusiveGateWay:1:90004
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
		//TaskQuery tqA=tq.taskUnassigned()//张三指定个人任务查询，指定办理人,办理人为空的时候未派职务的
				//.taskUnnassigned();
		//TaskQuery tqA=tq.taskAssignee("张三丰");//费用报销申请,部门经理李四
		//TaskQuery tqA=tq.taskAssignee("周芷若");//费用报销申请,部门经理李四
		//TaskQuery tqA=tq.taskAssignee("灭绝师太");//费用报销申请,部门经理李四
		TaskQuery tqA=tq.taskAssignee("张翠山");//费用报销申请,部门经理李四
		//TaskQuery tqA=tq.taskAssignee("孙七");
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
		String taskId="172504";
		Map v=new HashMap();
		//v.put("message", "不重要");
		//v.put("message", "重要");
		v.put("money", 800);
		processEngine.getTaskService().complete(taskId,v);;
		System.out.println("完成任务");
	}
	
	//(转办) 可以分配个人任务从一个人到另外一个人(认领任务)
	@Test
	public void setAssigneeTask(){
		String taskId="182504";
		//指定办理人
		String userId="张翠山";
		processEngine.getTaskService()
					.setAssignee(taskId, userId);;
	}
}

