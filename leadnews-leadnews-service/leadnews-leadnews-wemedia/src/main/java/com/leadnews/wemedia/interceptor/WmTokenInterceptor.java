package com.leadnews.wemedia.interceptor;

import com.leadnews.model.wemedia.pojos.WmUser;
import com.leadnews.utils.thread.WmThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WmTokenInterceptor implements HandlerInterceptor {

    /**
     * 寰楀埌header涓殑鐢ㄦ埛淇℃伅锛屽苟涓斿瓨鍏ュ埌褰撳墠绾跨▼涓?
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if(userId != null){
            //瀛樺叆鍒板綋鍓嶇嚎绋嬩腑
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtil.setUser(wmUser);

        }
        return true;
    }

    /**
     * 娓呯悊绾跨▼涓殑鏁版嵁
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        WmThreadLocalUtil.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WmThreadLocalUtil.clear();
    }
}

