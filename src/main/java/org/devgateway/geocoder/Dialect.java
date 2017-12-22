package org.devgateway.geocoder;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;


/**
* @author Sebastian Dimunzio
*/

public class Dialect extends org.hibernate.spatial.dialect.postgis.PostgisPG9Dialect {
    public Dialect() {
        super();
        this.registerFunction("simplify", new StandardSQLFunction("ST_Simplify"));
        this.registerFunction("simplifyPreserve", new StandardSQLFunction("ST_SimplifyPreserveTopology"));



    }
}
