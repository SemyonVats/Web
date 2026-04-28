<#-- @ftlvariable name="csrfToken" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">New Article</div>
        <div class="body">
            <form id="articleForm">
                <input type="hidden" name="_csrf" value="${csrfToken!}">
                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input id="title" name="title" type="text" required maxlength="255">
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="text" required minlength="10" rows="10"></textarea>
                    </div>
                </div>
                <div id="error" style="color: red; margin: 10px 0; min-height: 24px; text-align: center; width: 100%;"></div>
                <div class="button-field" style="text-align: center;">
                    <button type="submit" style="padding: 8px 24px;">Push</button>
                </div>
            </form>
        </div>
    </div>

    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/app.js"></script>
    <script>
        $(document).ready(function() {
            const $form = $('#articleForm');
            const $error = $('#error');
            const $submitBtn = $('button[type="submit"]');

            $form.on('submit', function(e) {
                e.preventDefault();
                $error.text('');

                window.ajax({
                    type: 'POST',
                    url: '/article?action=createJson',
                    data: $form.serialize(),
                    element: $submitBtn,
                    success: function(response) {
                        if (response.success) {
                            if (!response.redirect) {
                                window.location.href = '/index';
                            }
                        } else {
                            $error.text(response.error || 'Failed to create article');
                        }
                    },
                    error: function(xhr) {
                        let errorMsg = 'Failed to connect to server';
                        try {
                            const resp = JSON.parse(xhr.responseText);
                            errorMsg = resp.error || resp.message || 'Server error';
                        } catch (e) {
                            console.error('Parsing error response failed', e);
                        }
                        $error.text(errorMsg);
                    }
                });
            });
        });
    </script>
</@c.page>