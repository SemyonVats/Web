package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Notice;
import ru.itmo.wp.form.NoticeForm;
import ru.itmo.wp.form.validator.NoticeFormValidator;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AddNoticePage extends Page {
    private final NoticeService noticeService;
    private final NoticeFormValidator noticeFormValidator;

    public AddNoticePage(NoticeService noticeService, NoticeFormValidator noticeFormValidator) {
        this.noticeService = noticeService;
        this.noticeFormValidator = noticeFormValidator;
    }


    @InitBinder("noticeForm")
    public void initNoticeFormBinder(WebDataBinder binder) {
        binder.addValidators(noticeFormValidator);
    }

    @GetMapping("/notices/add")
    public String addNoticeForm(Model model) {
        model.addAttribute("noticeForm", new NoticeForm());
        return "AddNoticePage";
    }

    @PostMapping("/notices/add")
    public String addNotice(@Valid @ModelAttribute("noticeForm") NoticeForm noticeForm,
                            BindingResult bindingResult,
                            HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "AddNoticePage";
        }

        Notice notice = new Notice();
        notice.setContent(noticeForm.getContent());
        noticeService.save(notice);

        setMessage(httpSession, "Notice has been added successfully!");
        return "redirect:/notices/add";
    }
}