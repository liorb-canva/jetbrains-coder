package com.coder.gateway.views

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.ide.IdeBundle
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.ex.ActionManagerEx
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.ui.components.ActionLink
import org.jetbrains.annotations.Nls
import java.awt.datatransfer.StringSelection
import javax.swing.Icon

class LazyBrowserLink(icon: Icon, @Nls text: String) : ActionLink() {
    init {
        setIcon(icon, false)
        setText(text)
    }

    var url: String? = ""
        set(value) {
            field = value
            if (value != null) {
                addActionListener { BrowserUtil.browse(value) }
                ActionManagerEx.doWithLazyActionManager { instance ->
                    val group = DefaultActionGroup(OpenLinkInBrowser(value), CopyLinkAction(value))
                    componentPopupMenu = instance.createActionPopupMenu("popup@browser.link.context.menu", group).component
                }
            }
        }
}

private class CopyLinkAction(val url: String) : DumbAwareAction(IdeBundle.messagePointer("action.text.copy.link.address"), AllIcons.Actions.Copy) {

    override fun actionPerformed(event: AnActionEvent) {
        CopyPasteManager.getInstance().setContents(StringSelection(url))
    }
}

private class OpenLinkInBrowser(val url: String) : DumbAwareAction(IdeBundle.messagePointer("action.text.open.link.in.browser"), AllIcons.Nodes.PpWeb) {

    override fun actionPerformed(event: AnActionEvent) {
        BrowserUtil.browse(url)
    }
}