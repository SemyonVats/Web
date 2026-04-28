<#-- @ftlvariable name="login" type="java.lang.String" -->
<#-- @ftlvariable name="password" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="csrfToken" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Enter</div>
        <div class="body">
            <form id="loginForm">
                <input type="hidden" name="_csrf" value="${csrfToken!}">
                <div class="field">
                    <div class="name">
                        <label for="login">Login</label>
                    </div>
                    <div class="value">
                        <input id="login" name="login" value="${login!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" name="password" type="password" value="${password!}"/>
                    </div>
                </div>
                <div id="error" style="color: red; margin: 10px 0; min-height: 24px; text-align: center; width: 100%;"></div>
                <div class="button-field" style="text-align: center;">
                    <button type="submit" style="padding: 8px 24px;">Enter</button>
                </div>
            </form>
        </div>
    </div>

    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/app.js"></script>
    <script>
        $(document).ready(function() {
            const $form = $('#loginForm');
            const $error = $('#error');

            $form.on('submit', function(e) {
                e.preventDefault();
                $error.text('');

                window.ajax({
                    type: 'POST',
                    url: '/enter?action=loginJson',
                    data: $form.serialize(),
                    element: $('button[type="submit"]'),
                    success: function(response) {
                        if (!response.success) {
                            $error.text(response.error || 'Invalid credentials');
                        }
                    }
                });
            });
        });
    </script>
</@c.page>