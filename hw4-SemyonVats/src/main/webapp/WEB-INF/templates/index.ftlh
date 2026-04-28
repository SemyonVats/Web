<#import "./common.ftlh" as c>

<@c.page>
    <#list posts?sort_by("id")?reverse as post>
        <div class="post-item">
            <h2>${post.title}</h2>
            <#assign author = users?filter(user -> user.id == post.userId)?first>
            <p class="post-meta">${author.handle}</p>
            <p class="post-text">
                <#if post.text?length gt 350>
                    ${post.text[0..349]}...
                <#else>
                    ${post.text}
                </#if>
            </p>
            <hr>
        </div>
    </#list>
</@c.page>