package org.chronologicalthought;

import java.util.Hashtable;
import org.osgi.service.command.CommandProcessor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

  @SuppressWarnings("unchecked")
  public void start(BundleContext ctx) throws Exception {
    Hashtable props = new Hashtable();
    props.put(CommandProcessor.COMMAND_SCOPE, "ct");
    props.put(CommandProcessor.COMMAND_FUNCTION, new String[] { "starwars" });
    ctx.registerService(Starwars.class.getName(), new Starwars(), props);
  }

  public void stop(BundleContext ctx) throws Exception {
  }

}
