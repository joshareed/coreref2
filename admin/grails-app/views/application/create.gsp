<%@ page import="coreref.common.Application" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'application.label', default: 'Application')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:set var="errors" value="${app.errors}"/>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="appId"><g:message code="application.appId.label" default="App Id" /></label>
                                </td>
                                <td valign="top" class="value ${errors.appId ? 'errors' : ''}">
                                    <g:textField name="appId" value="${app?.appId ?: uuid}" />
									<span class="error">${errors.appId}</span>
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="contact"><g:message code="application.contact.label" default="Contact" /></label>
                                </td>
                                <td valign="top" class="value ${errors.contact ? 'errors' : ''}">
                                    <g:textField name="contact" value="${app?.contact}" />
									<span class="error">${errors.contact}</span>
                                </td>
                            </tr>
							<tr class="prop">
                                <td valign="top" class="name">
                                    <label for="site"><g:message code="application.site.label" default="Site" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:textField name="site" value="${app?.site}" />
                                </td>
                            </tr>
							<tr class="prop">
                                <td valign="top" class="name">
									<label for="enabled"><g:message code="application.enabled.label" default="Enabled" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:checkBox name="enabled" value="${true}" />
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
									<label for="limited"><g:message code="application.limited.label" default="Limited" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:checkBox name="limited" value="${false}" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
