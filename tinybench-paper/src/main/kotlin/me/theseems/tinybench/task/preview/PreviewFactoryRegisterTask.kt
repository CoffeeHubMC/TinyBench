package me.theseems.tinybench.task.preview

import me.theseems.tinybench.view.preview.IFPreviewViewFactory
import me.theseems.toughwiki.api.ToughWikiAPI
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask
import me.theseems.toughwiki.impl.bootstrap.Phase
import java.util.logging.Logger

class PreviewFactoryRegisterTask : BootstrapTask("previewFactoryRegister", Phase.POST_CONFIG) {
    override fun run(logger: Logger) {
        ToughWikiAPI.getInstance().viewManager.storeFactory(IFPreviewViewFactory())
    }
}
