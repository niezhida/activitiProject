package k_personalTask02;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskListanerImpl implements TaskListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//����ָ������İ�����
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		delegateTask.setAssignee("���ʦ̫");
	}

}
