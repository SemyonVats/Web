<#-- @ftlvariable name="articles" type="java.util.List<ru.itmo.wp.model.domain.Article>" -->
<#-- @ftlvariable name="csrfToken" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <h2>My Articles</h2>

    <#if articles?size == 0>
        <p>You don't have any articles yet.</p>
    <#else>
        <table border="1" style="width: 100%; border-collapse: collapse; margin-top: 20px;">
            <thead>
            <tr>
                <th style="padding: 10px; text-align: left;">ID</th>
                <th style="padding: 10px; text-align: left;">Title</th>
                <th style="padding: 10px; text-align: left;">Status</th>
                <th style="padding: 10px; text-align: left;">Actions</th>
            </tr>
            </thead>
            <tbody>
            <#list articles as article>
                <tr style="border-bottom: 1px solid #ddd;">
                    <td style="padding: 10px;">${article.id}</td>
                    <td style="padding: 10px;">${article.title}</td>
                    <td style="padding: 10px;">
                        <#if article.hidden>
                            <span style="color: #e53e3e;">Hidden</span>
                        <#else>
                            <span style="color: #38a169;">Visible</span>
                        </#if>
                    </td>
                    <td style="padding: 10px;">
                        <button class="toggle-hidden"
                                data-article-id="${article.id}"
                                data-hidden="${article.hidden?string('true', 'false')}"
                                style="padding: 5px 10px; background: ${article.hidden?string('#38a169', '#e53e3e')}; color: white; border: none; border-radius: 4px; cursor: pointer;">
                            ${article.hidden?string('Show', 'Hide')}
                        </button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>

    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/app.js"></script>
    <script>
        $(document).ready(function() {
            $(document).on('click', '.toggle-hidden', function(e) {
                e.preventDefault();

                const button = $(this);
                const articleId = button.data('article-id');
                const currentHidden = button.attr('data-hidden') === 'true';
                const newHidden = !currentHidden;

                window.ajax({
                    type: 'POST',
                    url: '/myArticles?action=toggleHidden',
                    data: {
                        _csrf: '${csrfToken!}',
                        articleId: articleId,
                        hidden: newHidden
                    },
                    beforeSend: function() {
                        button.prop('disabled', true).css('opacity', '0.7');
                    },
                    complete: function() {
                        button.prop('disabled', false).css('opacity', '1');
                    },
                    success: function(response) {
                        if (response.success) {
                            button.attr('data-hidden', newHidden);
                            button.text(newHidden ? 'Show' : 'Hide');
                            button.css('background', newHidden ? '#38a169' : '#e53e3e');

                            const statusCell = button.closest('tr').find('td:eq(2)');
                            statusCell.html(newHidden ?
                                '<span style="color: #e53e3e;">Hidden</span>' :
                                '<span style="color: #38a169;">Visible</span>');
                        } else {
                            alert('Error: ' + response.error);
                        }
                    },
                    error: function(xhr) {
                        let errorMsg = 'Server error';
                        try {
                            const resp = JSON.parse(xhr.responseText);
                            if (resp.error) errorMsg = resp.error;
                        } catch (e) {
                            console.error('Parsing error', e);
                        }
                        alert('Failed to update article status: ' + errorMsg);
                    }
                });
            });
        });
    </script>
</@c.page>