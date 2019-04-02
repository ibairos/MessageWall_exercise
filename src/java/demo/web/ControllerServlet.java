package demo.web;

import demo.impl.MessageWall_and_RemoteLogin_Impl;
import demo.impl.UserAccess_Impl;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String view = perform_action(request);
        forwardRequest(request, response, view);
    }

    protected String perform_action(HttpServletRequest request)
        throws IOException, ServletException {
        
        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();

        if (serv_path.equals("/login.do")) {
            if (session.getAttribute("useraccess") != null) {
                return "/view/wallview.jsp";
            }
            MessageWall_and_RemoteLogin_Impl remoteLogin = (MessageWall_and_RemoteLogin_Impl) getRemoteLogin();
            
            String user = request.getParameter("user");
            String password = request.getParameter("password");

            if (user != null && password != null) {
                UserAccess_Impl userAccess = remoteLogin.connect(user, password);
                if (userAccess == null) {
                    return "/error-not-loggedin.html";
                }
                session.setAttribute("useraccess", userAccess);
                return "/view/wallview.jsp";
            } else {
                return "/error-no-user_access.html";
            }  
        } 
        
        else if (serv_path.equals("/put.do")) {
            if (session.getAttribute("useraccess") != null) {
                UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
                String msg = request.getParameter("msg");
                userAccess.put(msg);
                return "/view/wallview.jsp";
            } else {
                return "/error-not-loggedin.html";
            }        
        } 
        
        else if (serv_path.equals("/refresh.do")) {
            if (session.getAttribute("useraccess") != null) {
                return "/view/wallview.jsp";
            } else {
                return "/error-not-loggedin.html";
            }
        } 
        
        else if (serv_path.equals("/logout.do")) {
            session.removeAttribute("useraccess");
            return "/goodbye.html";
        }
        
        else if (serv_path.equals("/delete.do")) {
            if (session.getAttribute("useraccess") != null) {
                UserAccess userAccess = (UserAccess) session.getAttribute("useraccess");
                int index = Integer.parseInt(request.getParameter("index"));
                userAccess.delete(index);
                return "/view/wallview.jsp";
            } else {
                return "/error-not-loggedin.html";
            }
        }
        
        else {
            return "/error-bad-action.html";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }
    
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '"+view+"'");
        }
        dispatcher.forward(request,response);
    }
}




