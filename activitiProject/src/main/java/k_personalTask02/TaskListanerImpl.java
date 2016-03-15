package k_personalTask02;

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
		delegateTask.setAssignee("灭绝师太");
	}

}
