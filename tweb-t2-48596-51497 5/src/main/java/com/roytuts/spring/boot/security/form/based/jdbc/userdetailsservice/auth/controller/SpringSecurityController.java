package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.controller;

import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.dao.EventDao;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.dao.UserDao;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.Event;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringSecurityController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EventDao eventDao;

    @GetMapping("/")
    public String events(@RequestParam(defaultValue = "0") int pageOngoing,
                         @RequestParam(defaultValue = "4") int sizeOngoing,
                         @RequestParam(defaultValue = "0") int pageFuture,
                         @RequestParam(defaultValue = "4") int sizeFuture,
                         @RequestParam(defaultValue = "0") int pagePast,
                         @RequestParam(defaultValue = "4") int sizePast,
                         Model model) {

        List<Event> allOngoingEvents = eventDao.findOngoingEvents();
        List<Event> allFutureEvents = eventDao.findFutureEvents();
        List<Event> allPastEvents = eventDao.findPastEvents();

        List<Event> ongoingEvents = eventDao.paginateEvents(allOngoingEvents, pageOngoing, sizeOngoing);
        List<Event> futureEvents = eventDao.paginateEvents(allFutureEvents, pageFuture, sizeFuture);
        List<Event> pastEvents = eventDao.paginateEvents(allPastEvents, pagePast, sizePast);

        model.addAttribute("ongoingEvents", ongoingEvents);
        model.addAttribute("futureEvents", futureEvents);
        model.addAttribute("pastEvents", pastEvents);

        model.addAttribute("pageOngoing", pageOngoing);
        model.addAttribute("sizeOngoing", sizeOngoing);
        model.addAttribute("pageFuture", pageFuture);
        model.addAttribute("sizeFuture", sizeFuture);
        model.addAttribute("pagePast", pagePast);
        model.addAttribute("sizePast", sizePast);

        return "index";
    }

    @GetMapping("/acessoReservado")
    public String acessoReservado(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(userDao.getUserRole(username).equals("ROLE_ATLETA")) {
            return "registerParticipant";
        } else {
            return "registerEvent";
        }
    }


    @GetMapping("/searchEvents")
    public String procurarEventos(Model model) {
        return "searchEvents";
    }

    @GetMapping("/registerEvent")
    public String registerEvent(Model model) {
        return "registerEvent";
    }

    @GetMapping("/registerTimestamp")
    public String registerTimestamp(Model model) {
        return "registerTimestamp";
    }

    @GetMapping("/registerParticipant")
    public String registerParticipant(Model model) {
        return "registerParticipant";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {


        List<String> currentUsers = userDao.getUsernameList();
        for (String currentUser : currentUsers) {
            if (currentUser.equals(username)) {
                model.addAttribute("error", "Nome de utilizador já existe!");
                return "register";
            }
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        User u = new User( username, encodedPassword, role);
        userDao.saveUser(u);
        model.addAttribute("successMessage", "Conta criada com sucesso!");
        return "register";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Credenciais erradas.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Logout com sucesso.");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(Model model, HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }


}
