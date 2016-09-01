package tommon.annotations;

/**
 * Created by majlen on 28.6.16.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.FIELD)
public @interface JMXCompositeMonitor {
    String value();
    String part();
}
