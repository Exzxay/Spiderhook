package com.github.mdelambilly.graphdbplugin.test.integration.neo4j.util.server;

/**
 * Lazily-initialised versions of Neo4j running in test containers.
 */
public enum Neo4jContainerServers {
    VERSION_5_26("5.26-community");

    private volatile Neo4jContainerServer instance;
    private final String versionTag;

    /**
     * Get the {@link Neo4jServer} instance for this version of Neo4j.
     * The instance is lazily initialised when this method is invoked for the first time.
     * @return a lazily-initialised {@link Neo4jServer} instance
     */
    public Neo4jServer getInstance() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = new Neo4jContainerServer(versionTag);
                    instance.start();
                }
            }
        }
        return instance;
    }

    Neo4jContainerServers(final String versionTag) {
        this.versionTag = versionTag;
    }
}
