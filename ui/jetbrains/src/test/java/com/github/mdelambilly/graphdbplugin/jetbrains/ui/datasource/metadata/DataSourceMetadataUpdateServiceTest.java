package com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.metadata;

import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.DataSourceType;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.DataSourceMetadata;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.metadata.DataSourcesComponentMetadata;
import com.github.mdelambilly.graphdbplugin.jetbrains.component.datasource.state.impl.DataSourceV1;
import com.github.mdelambilly.graphdbplugin.jetbrains.ui.datasource.tree.RootTreeNodeModel;
import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSourceMetadataUpdateServiceTest {

    private DataSourceMetadataUpdateService service;

    @BeforeEach
    public void setUp() throws Exception {

        final var metadataComponent = mock(DataSourcesComponentMetadata.class);
        final var treeUpdaters = mock(DataSourceTreeUpdaters.class);

        final var dataSourceMetadata = mock(DataSourceMetadata.class);
        when(metadataComponent.updateMetadata(any()))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(dataSourceMetadata)));

        final var treeUpdater = mock(DataSourceTreeUpdater.class);
        when(treeUpdaters.get(DataSourceType.NEO4J_BOLT))
                .thenReturn(Optional.of(treeUpdater));

        service = new DataSourceMetadataUpdateService(metadataComponent, treeUpdaters);
    }

    @Test
    public void updateDataSourceMetadataUi_withKnownDataSource_returnsTrue() throws Exception {
        final var root = new PatchedDefaultMutableTreeNode(RootTreeNodeModel.ROOT_NAME);
        final var dataSourceApi =
                new DataSourceV1("uuid", "local", DataSourceType.NEO4J_BOLT, new HashMap<>());

        final var future = service.updateDataSourceMetadataUi(root, dataSourceApi);

        final var result = future.get();

        assertTrue(result);
    }

    @Test
    public void updateDataSourceMetadataUi_withUnknownDataSource_returnsTrue() throws Exception {
        final var root = new PatchedDefaultMutableTreeNode(RootTreeNodeModel.ROOT_NAME);
        final var dataSourceApi =
                new DataSourceV1("uuid", "local", DataSourceType.UNKNOWN, new HashMap<>());

        final var future = service.updateDataSourceMetadataUi(root, dataSourceApi);

        final var result = future.get();

        assertFalse(result);
    }

}
