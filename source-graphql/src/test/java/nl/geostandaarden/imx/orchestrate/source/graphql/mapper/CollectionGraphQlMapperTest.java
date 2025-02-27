package nl.geostandaarden.imx.orchestrate.source.graphql.mapper;

import nl.geostandaarden.imx.orchestrate.engine.selection.SelectionBuilder;
import nl.geostandaarden.imx.orchestrate.model.Attribute;
import nl.geostandaarden.imx.orchestrate.model.Model;
import nl.geostandaarden.imx.orchestrate.model.ObjectType;
import nl.geostandaarden.imx.orchestrate.model.ObjectTypeRef;
import nl.geostandaarden.imx.orchestrate.model.Path;
import nl.geostandaarden.imx.orchestrate.model.Relation;
import nl.geostandaarden.imx.orchestrate.model.filters.FilterExpression;
import nl.geostandaarden.imx.orchestrate.model.types.ScalarTypes;
import nl.geostandaarden.imx.orchestrate.source.graphql.config.GraphQlOrchestrateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionGraphQlMapperTest {

    private CollectionGraphQlMapper collectionGraphQlMapper;

    @BeforeEach
    void init() {
        var config = GraphQlOrchestrateConfig.builder()
                .collectionSuffix("Collection")
                .build();

        collectionGraphQlMapper = new CollectionGraphQlMapper(config);
    }

    @Test
    void convert_returnsExpectedResult_withoutFilter() {
        var selection = SelectionBuilder.newCollectionNode(createModel(), "Nummeraanduiding")
                .select("naam")
                .selectObject("adres", builder -> builder //
                        .select("straat")
                        .select("huisnummer")
                        .build())
                .build();

        var request = selection.toRequest();
        var result = collectionGraphQlMapper.convert(request);

        var expected =
                """
        query Query {
          nummeraanduidingCollection {
            nodes {
              naam
              adres {
                straat
                huisnummer
              }
            }
          }
        }""";

        GraphQlAssert.assertThat(result.getQuery()).graphQlEquals(expected);
    }

    @Test
    void convert_returnsExpectedResult_withFilter() {
        var filter = FilterExpression.builder()
                .value("Kerkstraat")
                .path(Path.fromString("adres/straat"))
                .build();

        var selection = SelectionBuilder.newCollectionNode(createModel(), "Nummeraanduiding")
                .select("naam")
                .selectObject("adres", builder -> builder //
                        .select("straat")
                        .select("huisnummer")
                        .build())
                .filter(filter)
                .build();

        var request = selection.toRequest();
        var result = collectionGraphQlMapper.convert(request);

        var expected =
                """
        query Query {
          nummeraanduidingCollection(filter: {adres: { straat: { eq: "Kerkstraat" }}}) {
            nodes {
              naam
              adres {
                straat
                huisnummer
              }
            }
          }
        }""";

        GraphQlAssert.assertThat(result.getQuery()).graphQlEquals(expected);
    }

    private Model createModel() {
        return Model.builder()
                .objectType(ObjectType.builder()
                        .name("Nummeraanduiding")
                        .property(Attribute.builder()
                                .name("naam")
                                .type(ScalarTypes.STRING)
                                .build())
                        .property(Relation.builder()
                                .name("adres")
                                .target(ObjectTypeRef.forType("Adres"))
                                .build())
                        .build())
                .objectType(ObjectType.builder()
                        .name("Adres")
                        .property(Attribute.builder()
                                .name("straat")
                                .type(ScalarTypes.STRING)
                                .build())
                        .property(Attribute.builder()
                                .name("huisnummer")
                                .type(ScalarTypes.INTEGER)
                                .build())
                        .build())
                .build();
    }
}
