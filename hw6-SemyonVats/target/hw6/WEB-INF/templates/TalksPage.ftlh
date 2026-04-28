<#-- @ftlvariable name="talks" type="java.util.List<ru.itmo.wp.model.domain.Talk>" -->
<#-- @ftlvariable name="users" type="java.util.List<ru.itmo.wp.model.repository.dto.UserDto>" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Send Message</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="send"/>
                <div class="field">
                    <div class="name">
                        <label for="targetUserId">Recipient</label>
                    </div>
                    <div class="value">
                        <select id="targetUserId" name="targetUserId" required>
                            <#list users as user>
                                <option value="${user.id}">${user.login}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="text">Message</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="text" rows="5" required></textarea>
                    </div>
                </div>
                <#if error??>
                    <div class="error">
                        ${error}
                    </div>
                </#if>
                <div class="button-field">
                    <input type="submit" value="Send">
                </div>
            </form>
        </div>
    </div>

    <div class="datatable-box">
        <div class="header">Messages</div>
        <div class="body">
            <#if talks?has_content>
                <table class="datatable">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Text</th>
                        <th>When</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list talks as talk>
                        <tr>
                            <td>${talk.id}</td>
                            <td>${talk.sourceLogin}</td>
                            <td>${talk.targetLogin}</td>
                            <td>${talk.text}</td>
                            <td>${talk.creationTime}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            <#else>
                <div class="empty">No messages yet.</div>
            </#if>
        </div>
    </div>
</@c.page>