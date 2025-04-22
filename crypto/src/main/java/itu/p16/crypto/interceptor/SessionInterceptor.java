package itu.p16.crypto.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Récupérer la session sans en créer une nouvelle si elle n'existe pas
        HttpSession session = request.getSession(false);
        
        // Vérifier si la session existe et contient l'attribut "user"
        if (session == null || session.getAttribute("user") == null) {
            // Rediriger vers le contrôleur de login
            response.sendRedirect(request.getContextPath() + "/frontoffice/login?errorMessage=Vous+devez+vous+connecter");
            return false; // Interrompre la chaîne de traitement de la requête
        }
        return true; // Tout est OK, continuer la chaîne de traitement
    }

    private boolean isSessionValid(String sessionId) {
        return sessionId != null && !sessionId.isEmpty();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Non nécessaire pour cette logique, mais vous pouvez l'utiliser pour la modification de la réponse après l'exécution du contrôleur
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Cette méthode peut être utilisée pour libérer des ressources après la requête
    }
}

