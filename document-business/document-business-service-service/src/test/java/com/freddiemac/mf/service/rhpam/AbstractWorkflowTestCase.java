package com.freddiemac.mf.service.rhpam;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import org.drools.core.time.impl.PseudoClockScheduler;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Before;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * 
 * Base class for workflow unit tests
 * 
 */
public abstract class AbstractWorkflowTestCase extends JbpmJUnitBaseTestCase {
	private RuntimeManager runtimeManager;
	private RuntimeEngine runtimeEngine;
	protected KieSession kieSession;

	/*
	 * 
	 * List of process definitions to be tested
	 * 
	 */

	private String[] workflows = {
			"com/fm/found/rhpam_kick_start_p1/main0.bpmn"
	};

	/*
	 * 
	 * List of decision tables to be tested
	 * 
	 */

	private String[] decisionTables = {};
	public AbstractWorkflowTestCase() {
		super(true, true);
	}

	@Before
	public void init() {
		Map<String, ResourceType> resources = new HashMap<String, ResourceType>();
		for (String p : workflows) {
			resources.put(p, ResourceType.BPMN2);
		}

		for (String dt : decisionTables) {
			resources.put(dt, ResourceType.DTABLE);
		}

		// Using mock work item handlers
//		customHandlers.put("StartSubFlow", new StartProcessWorkItemHandlerMock());
//		customHandlers.put("ResumeParent", new NoOpWorkItemHandler());

		// Enable the PseudoClock using the following system property.
		System.setProperty("drools.clockType", "pseudo");

		if (runtimeManager == null) {
			runtimeManager = createRuntimeManager(Strategy.PROCESS_INSTANCE, resources);
		}

		runtimeEngine = getRuntimeEngine();
		kieSession = runtimeEngine.getKieSession();
	}

	/*
	 * 
	 * @After public void destroy() {
	 * 
	 * runtimeManager.disposeRuntimeEngine(runtimeEngine); runtimeManager.close(); }
	 * 
	 */

	protected ProcessInstance startProcess(String processDef, Map<String, Object> params) {
		ProcessInstance pInstance = kieSession.startProcess(processDef, params);
		assertNotNull(pInstance);
		return pInstance;
	}

	protected void signalProcess(Long pid, String signalName, Object data) {
		kieSession.signalEvent(signalName, data, pid);
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