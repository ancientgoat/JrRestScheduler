package com.premierinc.rule.rest;

/**
 *
 */
public class JrReturnEntity {

	private Boolean ruleAnswer;
	private String ruleStdout;
	private String ruleStderr;
	private Exception exception;

	private JrReturnEntity() {
	}

	public Boolean getRuleAnswer() {
		return ruleAnswer;
	}

	public String getRuleStdout() {
		return ruleStdout;
	}

	public String getRuleStderr() {
		return ruleStderr;
	}

	public Exception getException() {
		return exception;
	}

	/**
	 *
	 */
	public static class Builder {

		JrReturnEntity returnEntiy = new JrReturnEntity();

		public Builder setRuleAnswer(final Boolean inRuleAnswer) {
			this.returnEntiy.ruleAnswer = inRuleAnswer;
			return this;
		}

		public Builder setRuleStdout(final String inRuleStdout) {
			this.returnEntiy.ruleStdout = inRuleStdout;
			return this;
		}

		public Builder setRuleStderr(final String inRuleStderr) {
			this.returnEntiy.ruleStderr = inRuleStderr;
			return this;
		}

		public Builder setException(final Exception inException) {
			this.returnEntiy.exception = inException;
			return this;
		}

		public JrReturnEntity build() {
			return this.returnEntiy;
		}
	}
}
