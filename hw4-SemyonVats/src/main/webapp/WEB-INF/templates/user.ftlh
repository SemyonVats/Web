<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="currentUser" type="ru.itmo.web.hw4.model.User" -->
<#-- @ftlvariable name="prevUser" type="ru.itmo.web.hw4.model.User" -->
<#-- @ftlvariable name="nextUser" type="ru.itmo.web.hw4.model.User" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#assign u = currentUser! />

    <#if u??>
        <div class="profile-card">
            <div class="profile-text">
                <h1>${u.name!""}</h1>
                <ul>
                    <li>id: ${u.id!"N/A"}</li>
                    <li>handle: ${u.handle!""}</li>
                    <li>name: ${u.name!""}</li>
                </ul>
            </div>
            <img src="/img/no-title.jpg" alt="User Image">
        </div>


        <div class="nav-arrows-outside">
            <#if prevUser?? && prevUser.handle??>
                <a href="?handle=${prevUser.handle}" class="nav-arrow">←</a>
            <#else>
                <span class="nav-arrow disabled">←</span>
            </#if>
            <#if nextUser?? && nextUser.handle??>
                <a href="?handle=${nextUser.handle}" class="nav-arrow">→</a>
            <#else>
                <span class="nav-arrow disabled">→</span>
            </#if>
        </div>

    <#else>
        <p>No such user</p>
    </#if>
</@c.page>