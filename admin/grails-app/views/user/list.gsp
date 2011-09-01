<%@ page import="coreref.common.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <th>${message(code: 'user.firstName.label', default: 'First Name')}</th>
                            <th>${message(code: 'user.lastName.label', default: 'Last Name')}</th>
                            <th>${message(code: 'user.email.label', default: 'Email')}</th>
                            <th>${message(code: 'user.enabled.label', default: 'Enabled')}</th>
							<th>${message(code: 'user.roles.label', default: 'Roles')}</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${list}" status="i" var="user">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.email}</td>
                            <td><g:formatBoolean boolean="${user.enabled}" /></td>
							<td>${user.roles.join(', ')}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
