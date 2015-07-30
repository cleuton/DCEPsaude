package org.conserpro2015.saude;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SaudeApplication extends Application<SaudeConfiguration>{

    public static void main(String[] args) throws Exception {
        new SaudeApplication().run(args);
    }
	
    @Override
    public String getName() {
        return "saude";
    }
    
    @Override
    public void initialize(Bootstrap<SaudeConfiguration> bootstrap) {
    	bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }    
	
	@Override
	public void run(SaudeConfiguration configuration, Environment environment)
			throws Exception {
		
	
        final SaudeResource resource = new SaudeResource(
                configuration.getDbUrl()
            );
        
        final SnapshotResource resSnap = new SnapshotResource(
        		configuration.getDbUrl()
        		);
        		
        final SaudeCheck healthCheck =
                new SaudeCheck();
        environment.healthChecks().register("saude", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(resSnap);
	}

}

