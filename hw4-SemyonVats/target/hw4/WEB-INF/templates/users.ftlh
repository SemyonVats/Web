<#import "./common.ftlh" as c>

<@c.page>
    <h1>Users</h1>
    <table class="users-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Handle</th>
            <th>Name</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.id}</td>
                <td><@c.userlink user/></td>
                <td>${user.name}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>