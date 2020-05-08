package com.freddiemac.mf.service.rhpam;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.drools.core.time.impl.PseudoClockScheduler;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

/**
 * 
 * Base class for workflow unit tests
 * 
 */
public abstract class AbstractWorkflowTestCase extends JbpmJUnitBaseTestCase {
	private RuntimeManager runtimeManager;
	private RuntimeEngine runtimeEngine;
	protected KieSession kieSession;
	private TaskService taskService;

	/*
	 * 
	 * List of process definitions to be tested
	 * 
	 */

	private String[] workflows = {
			"document-business-service.bpmn"			
	};

	public AbstractWorkflowTestCase() {
		super(true, true);
	}

	@Before
	public void init() {
		Map<String, ResourceType> resources = new HashMap<String, ResourceType>();
		for (String p : workflows) {
			resources.put(p, ResourceType.BPMN2);
		}

//		for (String dt : decisionTables) {
//			resources.put(dt, ResourceType.DTABLE);
//		}

		// Using mock work item handlers
//		customHandlers.put("StartSubFlow", new StartProcessWorkItemHandlerMock());
//		customHandlers.put("ResumeParent", new NoOpWorkItemHandler());

		// Enable the PseudoClock using the following system property.
//		System.setProperty("drools.clockType", "pseudo");

		if (runtimeManager == null) {
			runtimeManager = createRuntimeManager(Strategy.PROCESS_INSTANCE, resources);
		}
		
		runtimeEngine = getRuntimeEngine();
		taskService = runtimeEngine.getTaskService();
		kieSession = runtimeEngine.getKieSession();
	}

	@After 
	public void destroy() {
		runtimeManager.disposeRuntimeEngine(runtimeEngine); 
		runtimeManager.close(); 
	}

	protected ProcessInstance startProcess(String processDef, Map<String, Object> params) {
		ProcessInstance pInstance = kieSession.startProcess(processDef, params);
		assertNotNull(pInstance);
		return pInstance;
	}
	
	protected void signalProcess(String type, Object event, Long pid) {
		kieSession.signalEvent(type, event, pid);
	}
	
	protected List<String> activeNodesInProcessInstance(long pid) {
		return getActiveNodesInProcessInstance(kieSession, pid);
	}
	
	// Human Task activities
	protected void executeTask(String userId, String taskName, long pid) {
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner(userId, "en-UK");
        TaskSummary task = list.get(0);
        System.out.println(userId + " is executing task {}" + task.getName());
        taskService.start(task.getId(), userId);
        taskService.complete(task.getId(), userId, null);

        assertNodeTriggered(pid, taskName);
	}
	
	protected List<Long> getTasksByProcessId(long processInstanceId) {
		List<Long> taskList = taskService.getTasksByProcessInstanceId(processInstanceId);
		return taskList;
	}
	
	protected void activateHumanTask(Long taskId, String userId) {
		taskService.activate(taskId, userId);
	}
	
	protected void claimHumanTask(long taskId, String userId) {
		taskService.claim(taskId, userId);
	}
	
	protected void completeHumanTask(long taskId, String userId, Map<String, Object> data) {
		taskService.complete(taskId, userId, data);
	}
	/**
	 * 
	 * Advances the timer by <amount> <timeUnit>
	 * 
	 * @param amount   - the amount
	 * 
	 * @param timeUnit - the time unit
	 * 
	 */

	protected void advanceTimer(long amount, TimeUnit timeUnit) {
		PseudoClockScheduler sessionClock = kieSession.getSessionClock();
		sessionClock.advanceTime(amount, timeUnit);
	}
}