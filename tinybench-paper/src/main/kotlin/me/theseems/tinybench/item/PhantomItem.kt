package me.theseems.tinybench.item

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper

data class PhantomItem(val content: ObjectNode = ObjectNode(YAMLMapper().nodeFactory)) : Item {
    override var amount: Int = 1
    override fun clone(): Item {
        return PhantomItem(content)
    }
}
