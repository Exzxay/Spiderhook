/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 * Modified by Michel de Lambilly, 2026
 */
package com.github.mdelambilly.graphdbplugin.jetbrains.component.updater;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.settings.SettingsComponent;
import com.github.mdelambilly.graphdbplugin.jetbrains.util.PluginUtil;
import com.github.mdelambilly.graphdbplugin.platform.GraphBundle;
import com.github.mdelambilly.graphdbplugin.platform.GraphConstants;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Displays a notification when the plugin was updated to a new version.
 */
public class PluginUpdateActivity implements ProjectActivity {

    private final AtomicBoolean isUpdateNotificationShown = new AtomicBoolean(false);

    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        final String currentVersion = PluginUtil.getVersion();
        final String knownVersion = SettingsComponent.getInstance().getKnownPluginVersion();

        final boolean isUpdated = !currentVersion.equals(knownVersion);
        if (isUpdated || GraphConstants.IS_DEVELOPMENT) {
            if (isUpdateNotificationShown.compareAndSet(false, true)) {
                SettingsComponent.getInstance().setKnownPluginVersion(currentVersion);
                showNotification(project, currentVersion);
            }
        }
        return Unit.INSTANCE;
    }

    private void showNotification(@NotNull final Project project, String currentVersion) {
        final NotificationGroup group = NotificationGroupManager.getInstance()
                .getNotificationGroup("graphdbplugin.notifications.update");

        if (group != null) {
            final Notification notification = group.createNotification(
                    GraphBundle.message("updater.title", currentVersion),
                    GraphBundle.message("updater.notification"),
                    NotificationType.INFORMATION
            );

            Notifications.Bus.notify(notification, project);
        }
    }
}
