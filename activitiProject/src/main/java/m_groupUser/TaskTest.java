package m_groupUser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
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
		
		//添加用户角色组(组织机构管理)
		IdentityService identityService=processEngine.getIdentityService();
		//创建角色
		Group group=new GroupEntity("总经理");
		identityService.saveGroup(group);
		Group group1=new GroupEntity("部门经理");
		identityService.saveGroup(group1);
		//创建用户
		User user=new UserEntity("张三");
		User user1=new UserEntity("李四");
		User user2=new UserEntity("王五");
		identityService.saveUser(user);
		identityService.saveUser(user1);
		identityService.saveUser(user2);
		
		//建立用户和角色的关联关系
		identityService.createMembership("张三", "部门经理");
		identityService.createMembership("李四", "部门经理");
		identityService.createMembership("王五", "总经理");
		System.out.println("添加组织机构成功");
	}
	
	@Test 
	public void statProcessInstance(){
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("task");// 与正在执行的流程实例和执行对象相关的Service  
		//Map v=new HashMap();
		//v.put("userId", "周芷若");
		//v.put("userIds", "大大,中中,小小");
		//ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("task",v)// 与正在执行的流程实例和执行对象相关的Service  
	            //.startProcessInstanceByKey("myProcess")
	            ;// 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID："+pi.getId());//流程实例ID：227501
		//流程实例ID：232501  流程定义ID:task:9:230004
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
		//小A,小B,小C,小D
	}
	
	/**
	 * 查询当前人的组任务
	 */
	@Test
	public void findMyGrouptask(){
		TaskService ts=processEngine.getTaskService();//正在执行的任务管理相关的Service
		TaskQuery tq=ts.createTaskQuery();//创建任务查询对象
		//TaskQuery tqA=tq.taskCandidateUser("张三");
		TaskQuery tqA=tq.taskCandidateUser("李四");
		//TaskQuery tqA=tq.taskCandidateUser("王五");
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
	
	/**
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonaltask(){
		TaskService ts=processEngine.getTaskService();//正在执行的任务管理相关的Service
		TaskQuery tq=ts.createTaskQuery();//创建任务查询对象
		TaskQuery tqA=tq.taskAssignee("张三");
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
		String taskId="232504";
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
	
	//查询正在执行的任务办理表,候选者类型
	@Test
	public void findRunPersonTask(){
		String taskId="190004";
		List<IdentityLink> list=processEngine.getTaskService()
			.getIdentityLinksForTask(taskId);
		
		if(list!=null&&list.size()>0){
			for(IdentityLink t:list){
				System.out.println("任务id:"+t.getTaskId());
				System.out.println("类型:"+t.getType());
				System.out.println("流程实例id:"+t.getProcessInstanceId());
				System.out.println("流程定义id:"+t.getProcessDefinitionId());
				System.out.println("流程实例Id:"+t.getProcessInstanceId());
				System.out.println("u_Id:"+t.getUserId());
			}
			
		}
	}
	
	//查询历史任务办理表  参与者
	@Test
	public void findHistoryPersonTask(){
		String taskId="190004";
		List<HistoricIdentityLink> list=processEngine.getHistoryService()
			.getHistoricIdentityLinksForTask(taskId);
		
		if(list!=null&&list.size()>0){
			for(HistoricIdentityLink t:list){
				System.out.println("任务id:"+t.getTaskId());
				System.out.println("类型:"+t.getType());
				System.out.println("流程实例id:"+t.getProcessInstanceId());
				System.out.println("流程实例Id:"+t.getProcessInstanceId());
				System.out.println("u_Id:"+t.getUserId());
			}
			
		}
	}
	
	//拾取任务，将组任务分给个人任务(待领取)  ，拾取后组任务就查不到,(组任务转为个人任务)
	@Test
	public void claim(){
		String taskId="232504",userId="张三";
		//分配个人任务(可以是主任务中的成员，也可以是非组任务的成员)
		processEngine.getTaskService().setAssignee(taskId, userId);
		
	}
	
	//将个人任务回退到组任务(前提，之前一定是组任务)
	@Test
	public void setAssigee(){
		String taskId="190004";
		//分配个人任务(可以是主任务中的成员，也可以是非组任务的成员)
		processEngine.getTaskService().setAssignee(taskId, null);
	} 
	
	//将个人任务回退到组任务添加成员	
	@Test
	public void addGroupUser(){//额，竟然可以追加相同的，多个相同的大H
		//任务id
		String taskId="190004";
		processEngine.getTaskService()
			.addCandidateUser(taskId, "大H");
	}
	
	//从组任务删除成员 ,可以删除相同多个的成员
	@Test
	public void deleteGroupUser(){
		//任务id
		String taskId="190004";
		processEngine.getTaskService()
			.deleteCandidateUser(taskId, "大H");
	}
	
	
	@Test 
	public void testException1(){
		try {
			System.out.println("开始");
			testException2();
		} catch (Exception e) {
			System.out.println("结束:"+e.getMessage());
		}
	}
	
	public void testException2() throws Exception{
		int flag=1;
		try {
			if (flag==1) {
				System.out.println("flag=1");
				throw new Exception("flag=1");
			}
		} catch (Exception e) {
			System.out.println("Exception1:"+e.getMessage());
			throw new Exception("触发事件或执行人逻辑流抛异常！\n");
		}
	}
}

