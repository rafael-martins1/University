package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.dao.EventDao;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.dao.UserDao;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.Event;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.EventParticipants;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.PaymentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserDao userDao;

    List<EventParticipants> participants = new ArrayList<>();


    @PostMapping("/registerEvent")
    public String registerEvent(@RequestParam String event_name,
                                @RequestParam String event_description,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date event_date,
                                @RequestParam double join_value,
                                Model model) {

        // Lógica para verificar se o evento já existe
        List<String> currentEventNames = eventDao.getEventNames();
        for (String currentEventName : currentEventNames) {
            if (currentEventName.equals(event_name)) {
                model.addAttribute("error", "Nome do evento já existe!");
                return "registerEvent";
            }
        }

        // O evento não existe, salva o novo evento
        Event event = new Event(event_name, event_description, event_date, join_value);
        eventDao.saveEvent(event);

        model.addAttribute("successMessage", "Evento registado com sucesso!");
        return "registerEvent";
    }

    @PostMapping("/registerParticipant")
    public String registerParticipant(@RequestParam String event_name,
                                      @RequestParam String participant_real_name,
                                      @RequestParam String participant_type,
                                      @RequestParam String participant_gender,
                                      Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String participant_username = authentication.getName();

        try {

            if (eventDao.getEventIdByName(event_name) != null) {
                List<String> currentParticipants = eventDao.getParticipantsForEvent(eventDao.getEventIdByName(event_name));

                for (String currentParticipant : currentParticipants) {
                    if (currentParticipant.equals(participant_username)) {
                        model.addAttribute("error", "Participante já registado para este evento!");
                        return "registerParticipant";
                    }
                }
            }
        } catch (EmptyResultDataAccessException e) {
            model.addAttribute("error", "Nome de Evento inexistente.");
            return "registerParticipant";
        }

        EventParticipants participant = new EventParticipants(eventDao.getEventIdByName(event_name), participant_username, participant_real_name, participant_type, participant_gender);
        eventDao.saveParticipant(participant);
        participants.add(participant);

        model.addAttribute("successMessage", "Participante registado com sucesso!");
        model.addAttribute("value", eventDao.getEventValueByName(event_name));
        model.addAttribute("event_id", eventDao.getEventIdByName(event_name));
        return "payment";

    }

    @PostMapping("/pay")
    public String pay(@RequestParam String event_id_pagar,
                        Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String participant_username = authentication.getName();

        int event_id_int = Integer.parseInt(event_id_pagar);

        userDao.updateIsPaid(participant_username, event_id_int);


        model.addAttribute("successMessage", "Inscrição paga com sucesso!");
        return "registerParticipant";
    }

    @PostMapping("/registerInfoPayment")
    public String registerInfoPayment(@RequestParam String entity,
                                        @RequestParam String reference,
                                        @RequestParam String value,
                                        @RequestParam String event_id,
                                        Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String participant_username = authentication.getName();
        int event_id_int = Integer.parseInt(event_id);
        double value_double = Double.parseDouble(value);
        eventDao.savePaymentInfo(participant_username, event_id_int, entity, reference, value_double);

        model.addAttribute("entidade", entity);
        model.addAttribute("referencia", reference);
        model.addAttribute("valor", value);
        model.addAttribute("event_id_pagar", event_id);

        return "payRegistration";
    }

    @PostMapping("/registerParticipantLate")
    public String registerParticipantLate(Model model) {

        model.addAttribute("error", "Atleta não pagou a inscrição!");

        return "registerParticipant";
    }

    @PostMapping("/payLate")
    public String payLate(@RequestParam String event_id_pagar,
                              Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String participant_username = authentication.getName();

        int event_id_int = Integer.parseInt(event_id_pagar);

        PaymentInfo info = userDao.getPaymentInfo(participant_username, event_id_int);

        model.addAttribute("info", info);
        model.addAttribute("event_id", event_id_pagar);
        model.addAttribute("successMessage", "Inscrição paga com sucesso!");
        return "payLate";
    }

    @PostMapping("/registerTimestamp")
    public String registerTimestamp(@RequestParam String event_name,
                                    @RequestParam String race_local,
                                    @RequestParam String participant_username,
                                    @RequestParam String time_timestamp,
                                    Model model) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        java.util.Date parsedDate = sdf.parse(time_timestamp);
        Time time = new Time(parsedDate.getTime());

        try {

            if (eventDao.getEventIdByName(event_name) != null) {
                List<String> currentParticipants = eventDao.getParticipantsForEvent(eventDao.getEventIdByName(event_name));

                for (String currentParticipant : currentParticipants) {
                    if (currentParticipant.equals(participant_username)){
                        if(eventDao.saveTimestampEntry(eventDao.getEventIdByName(event_name), race_local, time, participant_username) == 0) {
                            model.addAttribute("timeSuccess", "Timestamp registado com sucesso!");
                        } else if(eventDao.saveTimestampEntry(eventDao.getEventIdByName(event_name), race_local, time, participant_username) == 2){
                            model.addAttribute("timeError", "Timestamp já registado nesse ponto.");
                        } else {
                            model.addAttribute("timeError", "Atleta ainda não pagou a inscrição.");
                        }
                        return "registerTimestamp";
                    }
                }

            }

        } catch (EmptyResultDataAccessException e) {
            model.addAttribute("timeError", "Nome de Evento inexistente.");
            return "registerTimestamp";
        }


        model.addAttribute("timeError", "Participante não está inscrito no evento.");
        return "registerTimestamp";
    }

    @GetMapping("/checkRegistrations")
    public String checkRegistrations(Model model,
                                      Authentication authentication) throws JsonProcessingException {

        String events = eventDao.getEvents(authentication.getName());
        if (events.isEmpty()) {
            model.addAttribute("error", "Não está inscrito em nenhum evento!");
        } else {
            model.addAttribute("lista_eventos", events);
        }

        return "checkRegistrations";
    }

    @PostMapping("/searchEvents")
    public String searchEvents(@RequestParam String input,
                                  @RequestParam String searchType,
                                  Model model) throws JsonProcessingException, ParseException {
        String events;
        if (searchType.equals("nome")) {
            events = eventDao.searchEventsByName(input);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date data_convert = dateFormat.parse(input);
            events = eventDao.searchEventsByDate(data_convert);
        }
        if(events.equals("[]")) {
            model.addAttribute("error", "Evento não existe!");
        }
        model.addAttribute("lista_eventos", events);

        return "searchEvents";
    }

    @GetMapping("/eventDetails")
    public String eventDetails(@RequestParam String eventname, Model model) throws JsonProcessingException {

        List<EventParticipants> participantes = eventDao.getEventParticipantsByEventId(eventDao.getEventIdByName(eventname));
        List<EventParticipants> times = eventDao.updateParticipantsWithTimes(eventDao.getEventIdByName(eventname),participantes);
        times.sort(Comparator.comparing(EventParticipants::getTime));
        List<EventParticipants> time = eventDao.getAthleteTimes(eventDao.getEventIdByName(eventname), participantes);

        model.addAttribute("eventName", eventname);
        model.addAttribute("eventID", eventDao.getEventIdByName(eventname));
        model.addAttribute("participantes", participantes);
        model.addAttribute("times", times);
        model.addAttribute("tempos", time);

        return "eventDetails";
    }

}




