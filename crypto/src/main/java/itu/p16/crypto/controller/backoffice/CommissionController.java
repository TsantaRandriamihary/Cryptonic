package itu.p16.crypto.controller.backoffice;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import itu.p16.crypto.model.commission.Commission;
import itu.p16.crypto.service.commission.CommissionService;

@Controller
@RequestMapping("/backoffice/commission")
public class CommissionController {
    @Autowired
    private CommissionService commissionService;

    @GetMapping
    public String insertPage(Model model) {
        return "backoffice/commission";
    }

    @PostMapping
    public String insert(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
        @RequestParam("vente") double vente,
        @RequestParam("achat") double achat,
            Model model) {
        Timestamp datet = Timestamp.valueOf(date);
        Commission commission = new Commission(vente,achat, datet);
        commissionService.save(commission);
        return "redirect:/backoffice/crypto/list";
    }
}
