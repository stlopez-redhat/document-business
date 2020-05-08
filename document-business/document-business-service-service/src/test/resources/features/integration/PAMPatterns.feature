#@ignore
Feature: [RH PAM Pattern 1] Start flow and wait for completin before continuing

	Scenario: Starting teh main flow
		Given the main flow
		When I start the flow
		Then the subflow should be started
		And the main flow should wait for the sub flow