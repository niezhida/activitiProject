package l_group02;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListanerImpl implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用来指定任务的办理人
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		//个人任务
		//delegateTask.setAssignee("灭绝师太");
		//组任务
		delegateTask.addCandidateGroup("郭靖");
		delegateTask.addCandidateGroup("黄蓉");
		
	}

}
