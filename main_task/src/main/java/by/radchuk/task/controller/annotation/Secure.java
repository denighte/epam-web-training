package by.radchuk.task.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * NOTE: security level support is unavailable because of
 * custom payload in JWT verification.
 * This can be easily added by changing some logic in security filter.
 * For example you can create class which will extend/contain JWT class.
 * It will create some necessary payload and header values e.g. expires, typ, alg and ect.
 *
 * It was not implemented due to lack of time and bad custom handler support.
 * (Actually framework is not fully usable right now due to lack of
 * custom handler support (custom Context/RequestParam/ect. handlers).
 * All new handlers are hardcoded in old classes
 * (the most bad implementations are WebTaskImpl/ServletControllerStarter
 * so the logic of this classes should be changed and added some Listener/Handler classes
 * which will handle each type injection/ object creation. Due to this fact the WebTaskFactory class
 * also should be changed and it should inject these custom handlers)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    byte value() default 1;
}
