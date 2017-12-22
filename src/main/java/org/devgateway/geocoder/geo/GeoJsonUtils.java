package org.devgateway.geocoder.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geojson.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sebastian Dimunzio
 */

public final class GeoJsonUtils {

    private GeoJsonUtils() {

    }

    /**
     * @param geometry
     * @return GeoJsonObject
     * Can be: Feature, Geometry, LineString, MultiLineString, MultiPoint, MultiPolygon, Point, Polygon
     */
    public static GeoJsonObject jtsGeometryToGeoJson(final Geometry geometry) {
        Class<? extends Geometry> geometryClass = geometry.getClass();
        if (geometryClass.equals(com.vividsolutions.jts.geom.Point.class)) {
            return convert((com.vividsolutions.jts.geom.Point) geometry);
        } else if (geometryClass.equals(com.vividsolutions.jts.geom.LineString.class)) {
            return convert((com.vividsolutions.jts.geom.LineString) geometry);
        } else if (geometryClass.equals(com.vividsolutions.jts.geom.Polygon.class)) {
            return convert((com.vividsolutions.jts.geom.Polygon) geometry);
        } else if (geometryClass.equals(com.vividsolutions.jts.geom.MultiPoint.class)) {
            return convert((com.vividsolutions.jts.geom.MultiPoint) geometry);
        } else if (geometryClass.equals(com.vividsolutions.jts.geom.MultiPolygon.class)) {
            return convert((com.vividsolutions.jts.geom.MultiPolygon) geometry);
        } else {
            // not supported: MultiLineString.class and GeometryCollection.class
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    private static org.geojson.Point convert(final com.vividsolutions.jts.geom.Point point) {
        org.geojson.Point json = new org.geojson.Point();
        json.setCoordinates(convert(point.getCoordinate()));
        return json;
    }

    private static org.geojson.MultiPoint convert(final com.vividsolutions.jts.geom.MultiPoint multiPoint) {
        org.geojson.MultiPoint json = new org.geojson.MultiPoint();
        json.setCoordinates(convert(multiPoint.getCoordinates()));
        return json;
    }

    private static LineString convert(final com.vividsolutions.jts.geom.LineString line) {
        LineString json = new LineString();
        json.setCoordinates(convert(line.getCoordinates()));
        return json;
    }


    private static Polygon convert(final com.vividsolutions.jts.geom.Polygon polygon) {
        Polygon json = new Polygon();
        json.setExteriorRing(convert(polygon.getExteriorRing()).getCoordinates());
        return json;
    }


    private static MultiPolygon convert(final com.vividsolutions.jts.geom.MultiPolygon multiPolygon) {
        MultiPolygon json = new MultiPolygon();
        int size = multiPolygon.getNumGeometries();
        for (int i = 0; i < size; ++i) {
            json.add(convert((com.vividsolutions.jts.geom.Polygon) multiPolygon.getGeometryN(i)).getCoordinates());
        }
        return json;
    }

    private static LngLatAlt convert(final Coordinate coordinate) {
        return new LngLatAlt(coordinate.x, coordinate.y, coordinate.z);
    }


    private static List<LngLatAlt> convert(final Coordinate[] coordinates) {
        return Arrays.asList(coordinates).stream().map(c -> new LngLatAlt(c.x, c.y, c.z)).collect(Collectors.toList());
    }

}
