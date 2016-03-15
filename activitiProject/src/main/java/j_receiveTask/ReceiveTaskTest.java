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
	 
	/**部署流程定义inputStream*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		InputStream inBpmn =this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inPng =this.getClass().getResourceAsStream("receiveTask.png");
		Deployment dp=processEngine.getRepositoryService()
				.createDeployment()
				.name("接收活动任务")
				.addInputStream("receiveTask.bpmn", inBpmn)
				.addInputStream("receiveTask.png", inPng)
				.deploy();
		System.out.println("部署Id："+dp.getId());
		System.out.println("部署名称："+dp.getName());
		//部署Id：90001 部署名称：排他网关
	}
	
	@Test 
	public void statProcessInstance(){
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("receiveTask")// 与正在执行的流程实例和执行对象相关的Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID："+pi.getId());//流程实例ID 92501 流程实例ID：exclusiveGateWay:1:90004
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
		//查询执行对象id
		Execution e=processEngine.getRuntimeService().createExecutionQuery()
			.processInstanceId(pi.getId())
			.activityId("receivetask1").singleResult();//当前活动的id，  对应receivetask.bpmn文件中的id值
			
		//使用流程变量设置当日销售额，用来传递业务参数
		processEngine.getRuntimeService().setVariable(e.getId(), "汇总当日销售额", 21000);
		
		//向后执行异步，如果流程处于等待状态，使得流程继续执行
		processEngine.getRuntimeService().signal(e.getId());
		
		//查询执行对象id
		Execution e2=processEngine.getRuntimeService().createExecutionQuery()
				.processInstanceId(pi.getId())
				.activityId("receivetask2").singleResult();
		//从流程变量中获取汇总当日销售额的值
		Integer value=(Integer) processEngine.getRuntimeService().getVariable(e2.getId(), "汇总当日销售额");
		System.out.println("给老婆发送短信，金额是："+value);
		
		//向后执行异步，如果流程处于等待状态，使得流程继续执行
		processEngine.getRuntimeService().signal(e2.getId());
	}
}
