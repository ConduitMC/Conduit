package systems.conduit.main.plugin;

import systems.conduit.main.plugin.config.ConfigurationType;
import systems.conduit.main.plugin.config.annotation.ConfigFile;
import systems.conduit.main.plugin.config.annotation.Field;

/**
 * @author Innectic
 * @since 10/17/2019
 */
@ConfigFile(name = "test", type = ConfigurationType.JSON)
public class Test {

    @Field("testing")
    public class Testing {
        @Field("name")
        public String name;
    }

    @Field("thing")
    public String thing;
}
