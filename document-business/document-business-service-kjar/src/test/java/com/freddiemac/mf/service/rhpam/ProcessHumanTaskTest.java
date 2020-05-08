package com.freddiemac.mf.service.rhpam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessHumanTaskTest extends JbpmJUnitBaseTestCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessHumanTaskTest.class);

    public ProcessHumanTaskTest() {
        super(true, false);
    }

    @Test
    public void testProcessProcessInstanceStrategy() {
        RuntimeManager manager = createRuntimeManager(Strategy.PROCESS_INSTANCE, "manager", "document-business-service.bpmn");
        RuntimeEngine runtimeEngine = getRuntimeEngine(ProcessInstanceIdContext.get());
        KieSession ksession = runtimeEngine.getKieSession();
        TaskService taskService = runtimeEngine.getTaskService();
        Map<String, Object> params = new HashMap();
		params.put("documentReview", "documentReview");
		params.put("submitterTaskOwner", "rhpamAdmin");
		params.put("reviewTaskOwner", "rhpamAdmin");
		params.put("submitterTaskName", "submitterTaskName");
		params.put("reviewerTaskName", "reviewTaskName");
//		params.put("loanDocument", "rhpamAdmin");
//		params.put("loan", "rhpamAdmin");
		params.put("loanNumberId", "loanNumberId");
		params.put("reviewStatus", "reviewStatus");
		params.put("returnToSubmitter", "false");
		params.put("reviewApprover", "rhpamAdmin");

        ProcessInstance processInstance = ksession.startProcess("document-business-service-kjar.document-business-service", params);

        assertProcessInstanceActive(processInstance.getId(), ksession);
        
        String[] eventTypes = processInstance.getEventTypes();
        
//        assertNodeTriggered(processInstance.getId(), "StartNode", "Initialize");

        manager.disposeRuntimeEngine(runtimeEngine);
        runtimeEngine = getRuntimeEngine(ProcessInstanceIdContext.get(processInstance.getId()));

        ksession = runtimeEngine.getKieSession();
        taskService = runtimeEngine.getTaskService();

//        assertEquals(ksessionID, ksession.getId());

        // Task 1
        List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("rhpamAdmin", "en-UK");
        TaskSummary task = list.get(0);
        logger.info("rhpamAdmin is executing task {}", task.getName());
        taskService.start(task.getId(), "rhpamAdmin");
        taskService.complete(task.getId(), "rhpamAdmin", null);

        assertNodeTriggered(processInstance.getId(), "UploadDocumentTask");

        // Task 2
        list = taskService.getTasksAssignedAsPotentialOwner("rhpamAdmin", "en-UK");
        task = list.get(0);
        logger.info("rhpamAdmin is executing task {}", task.getName());
        taskService.start(task.getId(), "rhpamAdmin");
        taskService.complete(task.getId(), "rhpamAdmin", null);

        assertNodeTriggered(processInstance.getId(), "reviewerTask");
        
     // Task 3
        list = taskService.getTasksAssignedAsPotentialOwner("rhpamAdmin", "en-UK");
        task = list.get(0);
        logger.info("rhpamAdmin is executing task {}", task.getName());
        taskService.start(task.getId(), "rhpamAdmin");
        taskService.complete(task.getId(), "rhpamAdmin", null);

        assertNodeTriggered(processInstance.getId(), "ReviewConfirmationTask");
        assertProcessInstanceCompleted(processInstance.getId(), ksession);
    }
}