package com.premierinc.rule.scheduled;

import com.premierinc.rule.JrRestSchedApplication;
import com.premierinc.rule.base.SkRule;
import com.premierinc.rule.base.SkRuleMaster;
import com.premierinc.rule.run.SkRuleRunner;
import java.text.SimpleDateFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.premierinc.rule.JrRestSchedApplication.scheduledRuleRunner;

/**
 *
 */
@Component
public class JrScheduled {

	@Scheduled(fixedRate = 5000)
	public void yoYO() {
		SkRuleMaster ruleMaster = JrRestSchedApplication.scheduledRuleMaster;
		SkRuleRunner ruleRunner = scheduledRuleRunner;

		SkRule rule = ruleMaster.getRule("MILK_SCHEDULED_RULE_00001");
		scheduledRuleRunner.runRule(rule);
	}
}
