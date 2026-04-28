<#-- @ftlvariable name="message" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if message??>
        <div class="message">${message}</div>
    </#if>

    <h1>Articles</h1>
    <div id="articles-container" class="articles-list"></div>

    <template id="article-template">
        <article class="article-card">
            <div class="article-header">
                <h2 class="article-title"></h2>
                <div class="article-meta">
                    <span class="article-author">by <span class="author-name"></span></span>
                    <time class="article-date"></time>
                </div>
            </div>
            <div class="article-content"></div>
        </article>
    </template>

    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/app.js"></script>
    <script>
        $(document).ready(function() {
            function loadArticles() {
                const container = $('#articles-container');

                window.ajax({
                    url: '/index?action=articlesJson',
                    silent: true,
                    success: function(response) {
                        if (response.articles && response.articles.length > 0) {
                            renderArticles(response.articles);
                        } else {
                            container.html('<p class="empty-state">No articles yet.</p>');
                        }
                    },
                    error: function() {
                        container.html('<div class="error-message">Failed to load articles. Please try again later.</div>');
                    }
                });
            }

            function renderArticles(articles) {
                const container = $('#articles-container');
                const template = $('#article-template')[0].content;
                container.empty();

                articles.forEach(article => {
                    const articleElement = document.importNode(template, true);
                    articleElement.querySelector('.article-title').textContent = article.title;
                    articleElement.querySelector('.author-name').textContent = article.author.login;
                    articleElement.querySelector('.article-date').textContent = article.creationTime;
                    articleElement.querySelector('.article-content').textContent = article.text;
                    container.append(articleElement);
                });
            }
            loadArticles();
        });
    </script>

    <style>

        h1{
            font-size: 2rem;
        }

        .articles-list {
            display: grid;
            gap: 20px;
            margin: 20px 0;
        }

        .article-card {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            background: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            transition: box-shadow 0.3s ease;
        }

        .article-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .article-header {
            margin-bottom: 15px;
        }

        .article-title {
            font-size: 1.4rem;
            margin: 0 0 8px;
            color: #2d3748;
        }

        .article-meta {
            display: flex;
            gap: 15px;
            color: #718096;
            font-size: 0.9rem;
        }

        .article-content {
            line-height: 1.6;
            color: #4a5568;
            white-space: pre-wrap;
        }

        .empty-state {
            text-align: center;
            padding: 30px;
            color: #718096;
            font-style: italic;
        }

        .error-message {
            padding: 15px;
            background: #fff5f5;
            border-left: 4px solid #fc8181;
            color: #c53030;
            border-radius: 0 4px 4px 0;
        }

        @media (max-width: 768px) {
            .article-card {
                padding: 15px;
            }

            .article-title {
                font-size: 1.2rem;
            }
        }
    </style>
</@c.page>