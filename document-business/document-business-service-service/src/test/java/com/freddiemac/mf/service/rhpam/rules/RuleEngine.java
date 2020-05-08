package com.freddiemac.mf.service.rhpam.rules;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RuleEngine {
	private static KieServices ks = KieServices.Factory.get();
	private static KieContainer kc = ks.getKieClasspathContainer();
	
	public static void run(String ruleFlowGroup, Object... facts) {
		if(facts == null) {
			return;
		}
		
		KieSession ksession = kc.newKieSession();
		if(ruleFlowGroup != null && ruleFlowGroup.trim().isEmpty()) {
			ksession.getAgenda().getAgendaGroup(ruleFlowGroup).setFocus();
		}
		
		for(Object fact: facts) {
			ksession.insert(fact);
		}
		
		ksession.fireAllRules();
		ksession.dispose();
	}
}
