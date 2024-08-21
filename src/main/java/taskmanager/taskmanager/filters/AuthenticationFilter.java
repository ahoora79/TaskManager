//package taskmanager.taskmanager.filters;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//@WebFilter(filterName = "AuthenticationFilter", asyncSupported = true, urlPatterns = {"/*"})
//public class AuthenticationFilter implements Filter {
//    // URLs that don't require authentication
//    List<String> allowedURLs = Arrays.asList("login", "signup", "callback");
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        String servletPath = req.getServletPath();
//        HttpSession session = req.getSession(false); // Get session, but don't create a new one if it doesn't exist
//
//        // Debugging statements
//        System.out.println("Servlet Path: " + servletPath);
//        System.out.println("Session: " + session);
//        if (session != null) {
//            System.out.println("User in session: " + session.getAttribute("user"));
//        }
//
//        boolean loggedIn = (session != null && session.getAttribute("user") != null);
//
//        // If the user is trying to access login or signup and is already logged in, redirect to /home
//        if (servletPath.contains("login") || servletPath.contains("signup")) {
//            if (loggedIn) {
//                res.sendRedirect("/");
//                return;
//            }
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // For all other requests, if the user is not logged in, redirect to login page
//        if (!loggedIn) {
//            res.sendRedirect("/login");
//            return;
//        }
//
//        chain.doFilter(request, response);
//    }
//
//
//}