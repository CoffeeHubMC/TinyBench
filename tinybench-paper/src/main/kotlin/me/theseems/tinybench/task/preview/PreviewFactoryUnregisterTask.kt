package me.theseems.tinybench.task.preview

import me.theseems.tinybench.view.preview.IFPreviewViewFactory
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class PreviewFactoryUnregisterTask : BootstrapTask("previewFactoryUnregister", Phase.SHUTDOWN) {
    override fun run(logger: Logger) {
        ToughWikiAPI.getInstance().viewManager.removeFactory(IFPreviewViewFactory.typeName)
    }
}
