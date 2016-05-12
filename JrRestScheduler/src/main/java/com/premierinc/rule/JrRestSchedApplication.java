package com.premierinc.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.rule.base.SkRule;
import com.premierinc.rule.base.SkRuleBase;
import com.premierinc.rule.base.SkRuleMaster;
import com.premierinc.rule.base.SkRules;
import com.premierinc.rule.common.JsonMapperHelper;
import com.premierinc.rule.common.SkJsonWalkDir;
import com.premierinc.rule.run.SkRuleRunner;
import java.io.File;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 */
@SpringBootApplication
@EnableScheduling
public class JrRestSchedApplication {

	public static final String RULE_PATH = "rules";
	public static final String REST_RULE_PATH = String.format("/%s/rest_rules", RULE_PATH);
	public static final String SCHEDULED_RULE_PATH = String.format("%s/scheduled_rules", RULE_PATH);

	public static SkRuleMaster restRuleMaster;
	public static SkRuleRunner restRuleRunner;

	public static SkRuleMaster scheduledRuleMaster;
	public static SkRuleRunner scheduledRuleRunner;

	public static void main(String[] args) {
		readRestRules();
		SpringApplication.run(JrRestSchedApplication.class, args);
	}

	/**
	 *
	 */
	private static void readRestRules() {
		try {
			restRuleMaster = makeRuleMaster(String.format("%s/RestRule_0001.json", REST_RULE_PATH));
			restRuleRunner = restRuleMaster.getRuleRunner();
			scheduledRuleMaster = makeRuleMaster(String.format("%s/NumericOneRuleTest.json", SCHEDULED_RULE_PATH));
			scheduledRuleRunner = restRuleMaster.getRuleRunner();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static SkRuleMaster makeRuleMaster(String inStartingFilename) throws IOException {

		ClassPathResource resource = new ClassPathResource(inStartingFilename);
		File file = resource.getFile();
		SkJsonWalkDir jsonWalkDir = new SkJsonWalkDir(file);
		SkRuleMaster.Builder masterBuilder = new SkRuleMaster.Builder();
		ObjectMapper yamlMapper = JsonMapperHelper.newInstanceYaml();

		jsonWalkDir.getJsonPaths()
				.forEach(p -> {
					String json = jsonWalkDir.entryToString(p);
					try {
						masterBuilder.addRule(yamlMapper.readValue(json, SkRuleBase.class));
					} catch (Exception e) {
						try {
							masterBuilder.addRules(yamlMapper.readValue(json, SkRules.class));
						} catch (IOException inE) {
							throw new IllegalArgumentException(e);
						}
					}
				});

		return masterBuilder.build();
	}
}
