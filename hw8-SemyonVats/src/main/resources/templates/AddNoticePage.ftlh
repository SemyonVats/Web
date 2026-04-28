<#import "commons.ftlh" as c>
<#import "/spring.ftl" as spring>

<@c.page>
    <div class="add-notice form-box">
        <div class="header">Add New Notice</div>
        <div class="body">
            <form method="post" action="<@spring.url '/notices/add'/>">
                <div class="field">
                    <div class="name">
                        <label for="content">Notice Content</label>
                    </div>
                    <div class="value">
                        <textarea id="content" name="content" required>${(noticeForm.content)!''}</textarea>
                    </div>
                    <@spring.bind "noticeForm.content"/>
                    <#if spring.status.errorMessage??>
                        <div class="error">
                            ${spring.status.errorMessage}
                        </div>
                    </#if>
                </div>
                <div class="button-field">
                    <input type="submit" value="Add Notice">
                </div>
            </form>
        </div>
    </div>
</@c.page>