package scaldi

import _root_.play.api.GlobalSettings
import com.datastax.driver.core.Cluster
import org.slf4j.LoggerFactory
import scaldi.play.ScaldiSupport

object Global extends GlobalSettings with ScaldiSupport {

  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  val session = Cluster.builder().addContactPoint("127.0.0.1").build().connect("clash")

  // configure the injector
  override def applicationModule: Injector = new ControllerModule :: new BackendModule

}
