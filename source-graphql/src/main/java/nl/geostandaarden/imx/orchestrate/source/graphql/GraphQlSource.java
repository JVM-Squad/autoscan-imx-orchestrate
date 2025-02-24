package nl.geostandaarden.imx.orchestrate.source.graphql;

import lombok.RequiredArgsConstructor;
import nl.geostandaarden.imx.orchestrate.engine.source.DataRepository;
import nl.geostandaarden.imx.orchestrate.engine.source.Source;
import nl.geostandaarden.imx.orchestrate.source.graphql.config.GraphQlOrchestrateConfig;
import nl.geostandaarden.imx.orchestrate.source.graphql.executor.RemoteExecutor;
import nl.geostandaarden.imx.orchestrate.source.graphql.mapper.BatchGraphQlMapper;
import nl.geostandaarden.imx.orchestrate.source.graphql.mapper.CollectionGraphQlMapper;
import nl.geostandaarden.imx.orchestrate.source.graphql.mapper.ObjectGraphQlMapper;
import nl.geostandaarden.imx.orchestrate.source.graphql.mapper.ResponseMapper;
import nl.geostandaarden.imx.orchestrate.source.graphql.repository.GraphQlRepository;

@RequiredArgsConstructor
public class GraphQlSource implements Source {

    private final GraphQlOrchestrateConfig config;

    @Override
    public DataRepository getDataRepository() {
        return new GraphQlRepository(
                RemoteExecutor.create(config),
                new ObjectGraphQlMapper(config),
                new CollectionGraphQlMapper(config),
                new BatchGraphQlMapper(config),
                new ResponseMapper(config));
    }
}
