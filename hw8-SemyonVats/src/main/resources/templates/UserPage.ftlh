<#import "commons.ftlh" as c>

<@c.page>
    <#if errorMessage??>
        <div class="notification notification--error">
            ${errorMessage}
        </div>
    <#elseif user??>
        <div class="profile-card">
            <header class="profile-header">
                <h1 class="profile-title">Profile: ${user.login}</h1>
            </header>
            <div class="profile-content">
                <table class="user-details">
                    <tr>
                        <th class="user-details__label">ID:</th>
                        <td class="user-details__value">${user.id}</td>
                    </tr>
                    <tr>
                        <th class="user-details__label">Login:</th>
                        <td class="user-details__value">${user.login}</td>
                    </tr>
                    <tr>
                        <th class="user-details__label">Created at:</th>
                        <td class="user-details__value">${user.creationTime?string["dd MMMM yyyy HH:mm"]}</td>
                    </tr>
                </table>
            </div>
        </div>
    </#if>
</@c.page>