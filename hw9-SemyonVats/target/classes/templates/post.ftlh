<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="comment" type="ru.itmo.wp.domain.Comment" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="single-post">
        <@c.post post=post/>
    </div>

    <div class="post-tags">
        <strong>Теги:</strong>
        <div class="tag-list">
            <#if post.tags?has_content>
                <#list post.tags as tag>
                    <span class="tag">${tag.name}</span>
                </#list>
            <#else>
                <span>Нет тегов</span>
            </#if>
        </div>
    </div>

    <div class="comments-section">
        <h2>Комментарии (${post.comments?size})</h2>

        <#if post.comments?size == 0>
            <p class="no-comments">Пока нет комментариев</p>
        <#else>
            <#list post.comments as comment>
                <div class="comment">
                    <div class="comment-body">
                        <#list comment.text?split("\n") as line>
                            ${line}<#if line_has_next><br></#if>
                        </#list>
                    </div>
                    <div class="comment-info">
                        <span class="comment-author">${comment.user.login}</span>
                        <span class="comment-date">${comment.creationTime?string("dd.MM.yyyy HH:mm")}</span>
                    </div>
                </div>
            </#list>
        </#if>
    </div>

    <#if user??>
        <div class="add-comment">
            <h3>Добавить комментарий</h3>
            <form method="post" action="/post/${post.id}/comment">
                <div class="form-group">
                    <textarea name="text" rows="4" required placeholder="Ваш комментарий...">${comment.text!''}</textarea>
                    <#if bindingResult?has_content>
                        <#list bindingResult.getFieldErrors("text") as error>
                            <div class="error">${error.defaultMessage}</div>
                        </#list>
                    </#if>
                </div>
                <button type="submit" class="btn-submit">Отправить</button>
            </form>
        </div>
    <#else>
        <div class="login-prompt">
            <p>Чтобы оставлять комментарии, пожалуйста, <a href="/enter">войдите</a> в систему</p>
        </div>
    </#if>
</@c.page>