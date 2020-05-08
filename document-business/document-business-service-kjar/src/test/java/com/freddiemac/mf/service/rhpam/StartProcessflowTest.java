package com.freddiemac.mf.service.rhpam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.kie.api.runtime.process.ProcessInstance;

public class StartProcessflowTest extends AbstractWorkflowTestCase {
	private static final String PROCESS_DEF = "document-business-service-kjar.document-business-service";

	@Test
	public void processFlowTest() {
		String uuid = UUID.randomUUID().toString();
		
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
		
		// Start the process
		ProcessInstance pi = startProcess(PROCESS_DEF, params);
		Long pid = pi.getId();

		// Assert that the process instance is active
		assertProcessInstanceActive(pid);
		
//		assertNodeExists(pi, "Initialize");		
//		assertNodeActive(pid, kieSession, "Initialize");
		
//		assertNodeTriggered(pid, "Initialize");
//		executeTask("rhpamAdmin", "UploadDocumentTask", pid);
		
//		List<Long> taskList = getTasksByProcessId(pid);
//		System.out.println("******************************************************" + taskList.size());
//		for(Long taskId : taskList) {
//			System.out.println("------------------------- " + taskId);
//			activateHumanTask(taskId, null);
//			claimHumanTask(taskId, null);
//			completeHumanTask(taskId, null, null);
//		}
		
//		assertNodeTriggered(pid, "Initializing");
		
//		List<TaskSummary> list = taskService.
		
		//check if process instance has completed
//		assertProcessInstanceCompleted(pid);		
	}
}
