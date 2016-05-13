package com.premierinc.rule.rest;

import com.premierinc.rule.JrRestSchedApplication;
import com.premierinc.rule.base.SkRule;
import com.premierinc.rule.base.SkRuleMaster;
import com.premierinc.rule.run.SkGlobalContext;
import com.premierinc.rule.run.SkRuleRunner;
import com.premierinc.rule.utils.ExceptionHelper;
import com.premierinc.rule.utils.JrTeeStd;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 */
@RestController
@RequestMapping("/skunk")
public class JrMasterController {

	private Logger log = LoggerFactory.getLogger(JrMasterController.class);

	private static final String REST_RULE_01 = "MILK_REST_RULE_00001";

	/**
	 *
	 */
	@RequestMapping(value = "/global", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getGlobalMap() {
		TreeMap map = new TreeMap<String,Object>(SkGlobalContext.getGlobalMap());
		return new ResponseEntity(map, HttpStatus.OK);
	}

	/**
	 *
	 */
	@RequestMapping(value = "/runrule/{rulename}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity runRule(WebRequest webRequest, @PathVariable("rulename") String inRuleName) {

		JrTeeStd.tee();
		JrReturnEntity.Builder returnEntityBuilder = new JrReturnEntity.Builder();

		try {
			//
			//
			//
			Boolean answer = false;
			SkRuleMaster restRuleMaster = JrRestSchedApplication.restRuleMaster;
			SkRuleRunner restRuleRunner = JrRestSchedApplication.restRuleRunner;

			SkRule rule = restRuleMaster.getRule(inRuleName);

			Boolean ranRule = false;
			Map<String, String[]> params = webRequest.getParameterMap();
			for (Map.Entry<String, String[]> eSet : params.entrySet()) {

				String[] values = eSet.getValue();
				String value = "";
				if (0 < values.length) {
					value = values[0];
					if (!value.startsWith("'")) {
						try {
							BigDecimal bd = new BigDecimal(value);
							restRuleRunner.setGlobalValue(eSet.getKey(), bd);
							ranRule = true;
						} catch (Exception e) {
							ranRule = false;
						}
					}
				}
				if (!ranRule) {
					restRuleRunner.setGlobalValue(eSet.getKey(), value);
				}
			}

			answer = restRuleRunner.runRule(rule);

			returnEntityBuilder.setRuleAnswer(answer);

		} catch (Exception e) {
			returnEntityBuilder.setException(e);
			log.error(ExceptionHelper.toString(e));
			return new ResponseEntity(returnEntityBuilder.build(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			JrTeeStd.close();
			returnEntityBuilder.setRuleStdout(JrTeeStd.getOut());
			returnEntityBuilder.setRuleStderr(JrTeeStd.getErr());
		}

		return new ResponseEntity(returnEntityBuilder.build(), HttpStatus.OK);
	}

	//	/**
//	 *
//	 */
//	@RequestMapping(value = "/outrule/{rulename}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity oneRule(WebRequest webRequest, @PathVariable("rulename") String inRuleName) {
//		JrTeeStd.tee();
//		JrReturnEntity.Builder returnEntityBuilder = new JrReturnEntity.Builder();
//
//		try {
//			Boolean answer = false;
//			SkRuleMaster restRuleMaster = JrRestSchedApplication.restRuleMaster;
//			SkRuleRunner restRuleRunner = JrRestSchedApplication.restRuleRunner;
//
//			SkRule rule = restRuleMaster.getRule(inRuleName);
//
//			Boolean ranRule = false;
//			Map<String, String[]> params = webRequest.getParameterMap();
//			for (Map.Entry<String, String[]> eSet : params.entrySet()) {
//
//				String[] values = eSet.getValue();
//				String value = "";
//				if (0 < values.length) {
//					value = values[0];
//					if (!value.startsWith("'")) {
//						try {
//							BigDecimal bd = new BigDecimal(value);
//							restRuleRunner.setGlobalValue(eSet.getKey(), bd);
//							ranRule = true;
//						} catch (Exception e) {
//							ranRule = false;
//						}
//					}
//				}
//				if (!ranRule) {
//					restRuleRunner.setGlobalValue(eSet.getKey(), value);
//				}
//			}
//
//			answer = restRuleRunner.runRule(rule);
//
//			returnEntityBuilder.setRuleAnswer(answer);
//
//		} catch (Exception e) {
//			returnEntityBuilder.setException(e);
//			log.error(ExceptionHelper.toString(e));
//			return new ResponseEntity(returnEntityBuilder.build(), HttpStatus.INTERNAL_SERVER_ERROR);
//		} finally {
//			JrTeeStd.close();
//			returnEntityBuilder.setRuleStdout(JrTeeStd.getOut());
//			returnEntityBuilder.setRuleStderr(JrTeeStd.getErr());
//		}
//
//		return new ResponseEntity(returnEntityBuilder.build(), HttpStatus.OK);
//	}
//
}
