<#-- @ftlvariable name="users" type="java.util.List<ru.itmo.wp.model.domain.User>" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#-- @ftlvariable name="csrfToken" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">Users</div>

        <table class="main-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Login</th>
                <th>Creation Time</th>
                <th>Role</th>
                <#if user?? && user.admin>
                    <th>Actions</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr data-user-id="${u.id}">
                    <td>${u.id}</td>
                    <td>${u.login}</td>
                    <td>${u.creationTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td class="user-role">
                        <#if u.admin>
                            <span style="color: #e53e3e; font-weight: bold;">Admin</span>
                        <#else>
                            <span style="color: #38a169; font-weight: bold;">User</span>
                        </#if>
                    </td>
                    <#if user?? && user.admin>
                        <td>
                            <#if u.id != user.id>
                                <button class="admin-toggle"
                                        data-user-id="${u.id}"
                                        data-admin="${u.admin?string('true', 'false')}"
                                        style="margin-left: 10px; padding: 4px 10px;
                                                background: ${u.admin?string('#e53e3e', '#38a169')};
                                                color: white; border: none; border-radius: 4px;
                                                cursor: pointer; font-weight: bold;">
                                    ${u.admin?string('Revoke Admin', 'Make Admin')}
                                </button>
                            <#else>
                                <span style="margin-left: 10px; color: #999; font-style: italic;">(self)</span>
                            </#if>
                        </td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <#if user?? && user.admin>
        <script src="/js/jquery-3.7.1.min.js"></script>
        <script>
            $(document).ready(function() {
                $(document).on('click', '.admin-toggle', function(e) {
                    e.preventDefault();
                    e.stopPropagation();

                    const button = $(this);
                    const userId = button.data('user-id');
                    const currentAdmin = (button.data('admin') === 'true');
                    const newAdmin = !currentAdmin;

                    $.ajax({
                        type: 'POST',
                        url: '/users?action=toggleAdmin',
                        data: {
                            _csrf: '${csrfToken!}',
                            userId: userId,
                            admin: newAdmin
                        },
                        beforeSend: function() {
                            button.prop('disabled', true).css('opacity', '0.7');
                        },
                        success: function(response) {
                            if (response.success) {
                                button.data('admin', newAdmin.toString());
                                button.text(newAdmin ? 'Revoke Admin' : 'Make Admin');
                                button.css('background', newAdmin ? '#e53e3e' : '#38a169');

                                const roleSpan = button.closest('tr').find('.user-role span');
                                roleSpan.text(newAdmin ? 'Admin' : 'User');
                                roleSpan.css('color', newAdmin ? '#e53e3e' : '#38a169');
                            } else {
                                alert('Error: ' + (response.error || 'Unknown error'));
                            }
                        },
                        error: function(xhr, status, error) {
                            let errorMsg = 'Server error: ' + error;
                            try {
                                const resp = JSON.parse(xhr.responseText);
                                if (resp.error) errorMsg = resp.error;
                            } catch (e) {
                                console.error('Error parsing response', e);
                            }
                            alert('Failed to update admin status: ' + errorMsg);
                        },
                        complete: function() {
                            button.prop('disabled', false).css('opacity', '1');
                        }
                    });
                });
            });
        </script>
    </#if>

    <style>
        .main-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .main-table th,
        .main-table td {
            padding: 12px 15px;
            border: 1px solid #e2e8f0;
            text-align: left;
        }

        .main-table th {
            background-color: #f7fafc;
            font-weight: 600;
            color: #2d3748;
        }

        .main-table tr:nth-child(even) {
            background-color: #f8fafc;
        }

        .admin-toggle:hover:not(:disabled) {
            opacity: 0.9;
        }

        .admin-toggle:disabled {
            cursor: not-allowed;
        }

        .user-role {
            font-weight: bold;
        }
    </style>
</@c.page>