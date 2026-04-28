window.notify = function (message, type = 'success') {
    $.notify(message, {
        position: "right bottom",
        className: type
    });
};

window.ajax = function(options) {
    const defaults = {
        type: 'GET',
        dataType: 'json',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        complete: function() {
            $('body').css('cursor', 'default');

            if (options.element && options.element.data('ajax-disabled')) {
                options.element.prop('disabled', false);
                const originalText = options.element.data('original-text');
                if (originalText !== undefined) {
                    options.element.html(originalText);
                }
                options.element.removeData('ajax-disabled');
                options.element.removeData('original-text');
            }
        }
    };

    const settings = $.extend(true, {}, defaults, options);


    const originalSuccess = settings.success;
    const originalError = settings.error;

    settings.success = function(response) {
        if (response.redirect) {
            window.location.href = response.redirect;
            return;
        }

        if (response.success && response.message && !settings.silent) {
            window.notify(response.message, 'success');
        }

        if (typeof originalSuccess === 'function') {
            originalSuccess(response);
        }
    };


    settings.error = function(xhr) {
        let errorMsg = 'Server error';

        try {
            if (xhr.responseText) {
                const resp = JSON.parse(xhr.responseText);
                errorMsg = resp.error || resp.message || 'Server error';
            }
        } catch (e) {
            console.error('Parsing error response failed', e);
            errorMsg = 'Unknown server error';
        }

        if (!settings.silent) {
            window.notify(errorMsg, 'error');
        }

        if (typeof originalError === 'function') {
            originalError(xhr);
        }
    };

    return $.ajax(settings);
};