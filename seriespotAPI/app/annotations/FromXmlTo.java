package annotations;

import play.mvc.*;
import java.lang.annotation.*;

@With(XmlParsingAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FromXmlTo {

	Class<?> value() default Object.class;  

}