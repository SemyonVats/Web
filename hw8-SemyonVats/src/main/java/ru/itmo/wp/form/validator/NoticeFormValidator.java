package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.NoticeForm;

@Component
public class NoticeFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NoticeForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NoticeForm noticeForm = (NoticeForm) target;

        if (noticeForm.getContent() != null) {
            String content = noticeForm.getContent().trim();
        }
    }
}