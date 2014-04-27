package annotations;

import play.mvc.*;
import java.lang.annotation.*;

@With(JsonParsingAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FromJsonTo {

	Class<?> value() default Object.class;

}