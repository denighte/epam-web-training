package by.radchuk.task.controller.security;

import by.radchuk.task.controller.context.ControllerContext;
import by.radchuk.task.controller.exception.NotAuthorizedException;
import by.radchuk.task.controller.filter.RequestFilter;
import by.radchuk.task.controller.security.framework.JWT;
import by.radchuk.task.model.User;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.prefs.Preferences;

public class SecurityFilter implements RequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PAYLOAD_ATTRIBUTE_NAME = "JWTPayload";
    private static final Gson gson = new Gson();
    private Class payloadClass;
    private static final Preferences SECURITY_PREFERENCES
            = Preferences.userNodeForPackage(SecurityFilter.class);
    private static final String PAYLOAD_CLASS_KEY = "sec_filter_payload";

    @Override
    public void init(ControllerContext context) {
        try {
            payloadClass = Class.forName(SECURITY_PREFERENCES.get(PAYLOAD_CLASS_KEY, User.class.getName()));
        } catch (ClassNotFoundException exception) {
            payloadClass = User.class;
        }
    }

    @Override
    public void filter(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        JWT.Decoder decoder = JWT.decoder();
        if(token == null || !decoder.verify(token)) {
            throw new NotAuthorizedException();
        }
        try {
            request.setAttribute(PAYLOAD_ATTRIBUTE_NAME, decoder.decode(token));
        } catch (JWT.JWTDecodeException exception) {
            //impossible
            //because it would fail on verify function.
        }

    }

    public Class getPayloadClass() {
        return payloadClass;
    }

    public Object getPayload(HttpServletRequest request) {
        JWT jwt = (JWT)request.getAttribute(PAYLOAD_ATTRIBUTE_NAME);
        if (jwt != null) {
            return gson.fromJson(jwt.getPayloadJson(), payloadClass);
        }
        return null;
    }

}
