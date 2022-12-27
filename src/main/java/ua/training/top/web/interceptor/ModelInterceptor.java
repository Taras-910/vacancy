package ua.training.top.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor adds userTo to the model of every requests
 */
public class ModelInterceptor extends HandlerInterceptorAdapter {
/*

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !modelAndView.isEmpty()) {
            AuthorizedUser authorizedUser = SecurityUtil.safeGet();
            if (authorizedUser != null) {
                modelAndView.getModelMap().addAttribute("user", authorizedUser.getUser());
            }
        }
    }
*/
}
