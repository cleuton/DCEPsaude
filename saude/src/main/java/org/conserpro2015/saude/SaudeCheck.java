package org.conserpro2015.saude;

import com.codahale.metrics.health.HealthCheck;

public class SaudeCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		// TODO Auto-generated method stub
		return Result.healthy();
	}

}
