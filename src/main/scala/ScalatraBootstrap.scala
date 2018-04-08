import org.scalatra.LifeCycle
import configuration.ui.ConfigurationServer

import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext): Unit =
    context.mount(new ConfigurationServer, "/*")

}
