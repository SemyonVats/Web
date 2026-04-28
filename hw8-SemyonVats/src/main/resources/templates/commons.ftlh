<#import "/spring.ftl" as spring>

<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" type="text/css" href="<@spring.url '/css/normalize.css'/>">
        <link rel="stylesheet" type="text/css" href="<@spring.url '/css/style.css'/>">
        <link rel="stylesheet" type="text/css" href="<@spring.url '/css/form.css'/>">
        <link rel="stylesheet" type="text/css" href="<@spring.url '/css/datatable.css'/>">
        <script src="<@spring.url '/js/jquery-3.6.1.min.js'/>"></script>
        <script src="<@spring.url '/js/notify.min.js'/>"></script>
        <script src="<@spring.url '/js/app.js'/>"></script>
    </head>
    <body>
    <header>
        <a href="<@spring.url '/'/>">
            <img src="<@spring.url '/img/logo.png'/>" alt="Codeforces" title="Codeforces"/>
        </a>
        <div class="languages">
            <a href="?lang=en">
                <img src="<@spring.url '/img/gb.png'/>" alt="In English" title="In English"/>
            </a>
            <a href="?lang=ru">
                <img src="<@spring.url '/img/ru.png'/>" alt="In Russian" title="In Russian"/>
            </a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                ${user.login}
                |
                <a href="<@spring.url '/logout'/>">Logout</a>
            <#else>
                <a href="<@spring.url '/enter'/>">Enter</a>
                |
                <a href="<@spring.url '/register'/>">Register</a>
            </#if>
        </div>
        <nav>
            <ul>
                <li><a href="<@spring.url '/'/>">Home</a></li>
                <li><a href="<@spring.url '/users/all'/>">Users</a></li>
                <#if user??>
                    <li><a href="<@spring.url '/notices/add'/>">Add Notice</a></li>
                </#if>
            </ul>
        </nav>
    </header>
    <div class="middle">
        <aside>
            <#if notices?has_content>
                <#list notices as notice>
                    <section>
                        <div class="header">
                            Notice
                            <span class="notice-date">${notice.creationTime?string("dd.MM.yyyy")}</span>
                        </div>
                        <div class="notice-text">
                            <#list notice.content?split("\n") as line>
                                <#if line?trim?has_content>
                                    ${line?trim}<#if line_has_next><br></#if>
                                </#if>
                            </#list>
                        </div>
                    </section>
                </#list>
            <#else>
                <section>
                    <div class="header">
                        Notices
                    </div>
                    <div class="body">
                        No active notices
                    </div>
                </section>
            </#if>
        </aside>
        <main>
            <#nested/>
        </main>
    </div>
    <footer>
        <a href="/">Ejudge</a> &copy; 2077 by Mike Mirzayanov
    </footer>
    <script>
        $(function () {
            <#if message??>
                notify("${message?js_string}");
            </#if>
        })
    </script>
    </body>
    </html>
</#macro>

<#macro error field>
    <@spring.bind field/>
    <#if spring.status.errorMessage??>
        <div class="error">${spring.status.errorMessage}</div>
    </#if>
</#macro>