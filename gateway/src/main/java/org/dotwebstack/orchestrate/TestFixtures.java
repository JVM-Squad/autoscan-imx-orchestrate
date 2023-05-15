package org.dotwebstack.orchestrate;

import static org.dotwebstack.orchestrate.model.Cardinality.INFINITE;

import java.io.InputStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dotwebstack.orchestrate.ext.spatial.GeometryType;
import org.dotwebstack.orchestrate.model.Attribute;
import org.dotwebstack.orchestrate.model.Cardinality;
import org.dotwebstack.orchestrate.model.Model;
import org.dotwebstack.orchestrate.model.ModelMapping;
import org.dotwebstack.orchestrate.model.ObjectType;
import org.dotwebstack.orchestrate.model.ObjectTypeRef;
import org.dotwebstack.orchestrate.model.Relation;
import org.dotwebstack.orchestrate.model.types.ScalarTypes;
import org.dotwebstack.orchestrate.parser.yaml.YamlModelMappingParser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class TestFixtures {

  public enum TargetModelType {
    IMXGEO, CORELOCATION
  }

  public static Model createBagModel() {
    return Model.builder()
        .alias("bag")
        .objectType(ObjectType.builder()
            .name("Nummeraanduiding")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("huisnummer")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("huisnummertoevoeging")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("huisletter")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("postcode")
                .type(ScalarTypes.STRING)
                .build())
            .property(Relation.builder()
                .name("ligtAan")
                .target(ObjectTypeRef.forType("OpenbareRuimte"))
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Relation.builder()
                .name("ligtIn")
                .target(ObjectTypeRef.forType("Woonplaats"))
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("OpenbareRuimte")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("naam")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Relation.builder()
                .name("ligtIn")
                .target(ObjectTypeRef.forType("Woonplaats"))
                .cardinality(Cardinality.REQUIRED)
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("Woonplaats")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("naam")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("Pand")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("oorspronkelijkBouwjaar")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("status")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("geometrie")
                .type(new GeometryType())
                .cardinality(Cardinality.REQUIRED)
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("Verblijfsobject")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Relation.builder()
                .name("heeftAlsHoofdadres")
                .target(ObjectTypeRef.forType("Nummeraanduiding"))
                .cardinality(Cardinality.REQUIRED)
                .inverseName("isHoofdadresVan")
                .inverseCardinality(Cardinality.REQUIRED)
                .build())
            .property(Relation.builder()
                .name("heeftAlsNevenadres")
                .target(ObjectTypeRef.forType("Nummeraanduiding"))
                .cardinality(Cardinality.MULTI)
                .inverseName("isNevenadresVan")
                .inverseCardinality(Cardinality.OPTIONAL)
                .build())
            .property(Relation.builder()
                .name("maaktDeelUitVan")
                .target(ObjectTypeRef.forType("Pand"))
                .cardinality(Cardinality.of(1, INFINITE))
                .inverseName("bevat")
                .inverseCardinality(Cardinality.MULTI)
                .build())
            .build())
        .build();
  }

  public static Model createBgtModel() {
    return Model.builder()
        .alias("bgt")
        .objectType(ObjectType.builder()
            .name("Pand")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("bgt-status")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("geometrie2dGrondvlak")
                .type(new GeometryType())
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("identificatieBAGPND")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Relation.builder()
                .name("isGerelateerdAan")
                .target(ObjectTypeRef.fromString("bag:Pand"))
                .cardinality(Cardinality.OPTIONAL)
                .inverseName("isGerelateerdAan")
                .inverseCardinality(Cardinality.OPTIONAL)
                .build())
            .build())
        .build();
  }

  public static Model createBrkModel() {
    return Model.builder()
        .alias("brk")
        .objectType(ObjectType.builder()
            .name("Perceel")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("kadastraleGrootte")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("koopsom")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("begrenzing")
                .type(new GeometryType())
                .cardinality(Cardinality.REQUIRED)
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("LocatieKadastraalObject")
            .property(Relation.builder()
                .name("heeft")
                .identifier(true)
                .target(ObjectTypeRef.fromString("Perceel"))
                .cardinality(Cardinality.REQUIRED)
                .inverseName("heeftInverse")
                .inverseCardinality(Cardinality.MULTI)
                .build())
            .property(Relation.builder()
                .name("betreft")
                .identifier(true)
                .target(ObjectTypeRef.fromString("bag:Nummeraanduiding"))
                .cardinality(Cardinality.REQUIRED)
                .inverseName("betreftInverse")
                .inverseCardinality(Cardinality.MULTI)
                .build())
            .build())
        .build();
  }

  public static ModelMapping createModelMapping(TargetModelType targetModelType, InputStream mappingInputStream) {
    Model targetModel = null;

    if (targetModelType == TargetModelType.IMXGEO) {
      targetModel = createImxGeoModel();
    } else if (targetModelType == TargetModelType.CORELOCATION) {
      targetModel = createCoreLocationModel();
    }

    var yamlMapper = YamlModelMappingParser.getInstance();

    // TODO: Merge into one step
    return yamlMapper.parse(mappingInputStream)
        .toBuilder()
        .targetModel(targetModel)
        .sourceModel(createBagModel())
        .sourceModel(createBgtModel())
        .sourceModel(createBrkModel())
        .build();
  }

  private static Model createImxGeoModel() {
    return Model.builder()
        .alias("imxgeo")
        .objectType(ObjectType.builder()
            .name("Adres")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("huisnummer")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("huisnummertoevoeging")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("huisletter")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("postcode")
                .type(ScalarTypes.STRING)
                .build())
            .property(Attribute.builder()
                .name("straatnaam")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("plaatsnaam")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("isHoofdadres")
                .type(ScalarTypes.BOOLEAN)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("omschrijving")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("Gebouw")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("bouwjaar")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("maaiveldgeometrie")
                .type(new GeometryType())
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("bovenaanzichtgeometrie")
                .type(new GeometryType())
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Relation.builder()
                .name("heeftAlsAdres")
                .target(ObjectTypeRef.forType("Adres"))
                .cardinality(Cardinality.MULTI)
                .inverseName("isAdresVanGebouw")
                .inverseCardinality(Cardinality.of(1, INFINITE))
                .build())
            .property(Attribute.builder()
                .name("totaalPerceelOppervlak")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Relation.builder()
                .name("bevindtZichOpPerceel")
                .target(ObjectTypeRef.forType("Perceel"))
                .cardinality(Cardinality.of(1, INFINITE))
                .build())
            .build())
        .objectType(ObjectType.builder()
            .name("Perceel")
            .property(Attribute.builder()
                .name("identificatie")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.REQUIRED)
                .identifier(true)
                .build())
            .property(Attribute.builder()
                .name("oppervlak")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.REQUIRED)
                .build())
            .property(Attribute.builder()
                .name("laatsteKoopsom")
                .type(ScalarTypes.INTEGER)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("begrenzing")
                .type(new GeometryType())
                .cardinality(Cardinality.REQUIRED)
                .build())
            .build())
        .build();
  }

  private static Model createCoreLocationModel() {
    return Model.builder()
        .alias("loc")
        .objectType(ObjectType.builder()
            .name("Address")
//            .property(Attribute.builder()
//                .name("_id")
//                .type(ScalarTypes.STRING)
//                .cardinality(Cardinality.REQUIRED)
//                .build())
//            .property(Attribute.builder()
//                .name("addressArea")
//                .type(ScalarTypes.STRING)
//                .cardinality(Cardinality.MULTI)
//                .build())
            .property(Attribute.builder()
                .name("addressID")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .identifier(true)
                .build())
//            .property(Attribute.builder()
//                .name("adminUnitL1")
//                .type(ScalarTypes.STRING)
//                .cardinality(Cardinality.MULTI)
//                .build())
//            .property(Attribute.builder()
//                .name("adminUnitL2")
//                .type(ScalarTypes.STRING)
//                .cardinality(Cardinality.MULTI)
//                .build())
            .property(Attribute.builder()
                .name("fullAddress")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("locatorDesignator")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
//            .property(Attribute.builder()
//                .name("locatorName")
//                .type(ScalarTypes.STRING)
//                .cardinality(Cardinality.MULTI)
//                .build())
//            .property(Attribute.builder()
//                .name("poBox")
//                .type(ScalarTypes.STRING)
//                .cardinality(Cardinality.MULTI)
//                .build())
            .property(Attribute.builder()
                .name("postCode")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("postName")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .property(Attribute.builder()
                .name("thoroughfare")
                .type(ScalarTypes.STRING)
                .cardinality(Cardinality.OPTIONAL)
                .build())
            .build())
        .build();
  }
}
